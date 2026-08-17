package com.grocery.billing.share

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.grocery.billing.money.Money
import com.grocery.billing.print.BillLine
import java.io.File
import java.io.FileOutputStream

/**
 * Renders a bill as a PDF and exposes it through a FileProvider content:// URI
 * so other apps (e.g. WhatsApp) can attach it as a document.
 *
 * The whole page is drawn directly onto the [PdfDocument] canvas in PDF point
 * space (no bitmap scaling step), with every element's position derived from
 * the actual measured height of the element(s) drawn before it. Nothing uses
 * a fixed Y coordinate, so rows never overlap regardless of how much text
 * they contain, and content that doesn't fit on the current page automatically
 * flows onto a new page with the table header repeated.
 */
object BillPdf {

    // ── Page geometry (PDF points, 72pt = 1 inch; A4 = 595 x 842) ──────
    private const val PAGE_WIDTH = 595f
    private const val PAGE_HEIGHT = 842f
    private const val MARGIN = 36f

    private const val CONTENT_LEFT = MARGIN
    private const val CONTENT_RIGHT = PAGE_WIDTH - MARGIN
    private const val CONTENT_WIDTH = CONTENT_RIGHT - CONTENT_LEFT

    // Bottom of the safe drawing area. Anything below this triggers a page break.
    // Leaves room for the small "Page N" footer that sits in the outer margin.
    private const val BOTTOM_LIMIT = PAGE_HEIGHT - MARGIN

    // ── Product table columns (fixed, left→right: Name | Qty | Price | Amount) ──
    private const val COL_GAP = 8f
    private const val QTY_W = 46f
    private const val PRICE_W = 78f
    private const val AMOUNT_W = 90f
    private const val NAME_W = CONTENT_WIDTH - QTY_W - PRICE_W - AMOUNT_W - COL_GAP * 3

    private const val NAME_LEFT = CONTENT_LEFT
    private const val NAME_RIGHT = NAME_LEFT + NAME_W
    private const val QTY_LEFT = NAME_RIGHT + COL_GAP
    private const val QTY_RIGHT = QTY_LEFT + QTY_W
    private const val PRICE_LEFT = QTY_RIGHT + COL_GAP
    private const val PRICE_RIGHT = PRICE_LEFT + PRICE_W
    private const val AMOUNT_LEFT = PRICE_RIGHT + COL_GAP
    private const val AMOUNT_RIGHT = AMOUNT_LEFT + AMOUNT_W // == CONTENT_RIGHT

    // ── Font sizes (pt) ─────────────────────────────────────────────────
    private const val SIZE_SHOP_NAME = 16f
    private const val SIZE_SUBHEADER = 9f
    private const val SIZE_BILL_INFO = 9.5f
    private const val SIZE_TABLE_HEADER = 9f
    private const val SIZE_ITEM = 9f
    private const val SIZE_TOTALS = 10f
    private const val SIZE_GRAND_TOTAL = 12.5f
    private const val SIZE_FOOTER = 8.5f
    private const val MIN_FIT_SIZE = 6.5f

    private const val ROW_PAD_TOP = 6f
    private const val ROW_PAD_BOTTOM = 6f

    private val COLOR_GRAY_LINE = Color.parseColor("#888888")
    private val COLOR_THIN_LINE = Color.parseColor("#CCCCCC")
    private val COLOR_HEADER_BG = Color.parseColor("#F0F0F0")
    private val COLOR_ROW_ALT_BG = Color.parseColor("#FAFAFA")
    private val COLOR_TOTAL_BG = Color.parseColor("#F0F8F0")
    private val COLOR_TOTAL_VALUE = Color.parseColor("#1B5E20")
    private val COLOR_FOOTER_TEXT = Color.parseColor("#666666")

    fun generateShareUri(context: Context, data: ShareBillData): Uri? {
        val dir = File(context.cacheDir, "shared_bills").apply { mkdirs() }
        val safeNumber = data.billNumber.replace(Regex("[^A-Za-z0-9_-]"), "_").ifBlank { "bill" }
        val file = File(dir, "bill_$safeNumber.pdf")
        return try {
            FileOutputStream(file).use { out -> writePdf(data, out) }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            null
        }
    }

    // ── Paint helpers ────────────────────────────────────────────────

    private fun textPaint(
        size: Float,
        bold: Boolean = false,
        color: Int = Color.BLACK,
        italic: Boolean = false,
        align: Paint.Align = Paint.Align.LEFT
    ): TextPaint {
        val style = when {
            bold && italic -> Typeface.BOLD_ITALIC
            bold -> Typeface.BOLD
            italic -> Typeface.ITALIC
            else -> Typeface.NORMAL
        }
        return TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size
            typeface = Typeface.create(Typeface.DEFAULT, style)
            this.color = color
            textAlign = align
        }
    }

    /** Full line height (top of line to top of next line) for a single-line paint. */
    private fun lineHeight(p: Paint): Float {
        val fm = p.fontMetrics
        return fm.descent - fm.ascent
    }

    /** Distance from the top of a text line down to its drawing baseline. */
    private fun baselineFromTop(p: Paint): Float = -p.fontMetrics.ascent

    /** Draws a single line of text with its top edge at [topY], respecting paint.textAlign. */
    private fun drawSingleLine(canvas: Canvas, text: String, p: Paint, x: Float, topY: Float) {
        canvas.drawText(text, x, topY + baselineFromTop(p), p)
    }

    /**
     * Ensures [text] fits within [maxWidth] on a single line: first shrinks the font size down
     * to [MIN_FIT_SIZE], then falls back to an ellipsis if it still doesn't fit. Returns the
     * (possibly adjusted) paint and the (possibly truncated) text to draw. Guarantees the
     * returned text never paints outside its column.
     */
    private fun fitSingleLine(text: String, base: TextPaint, maxWidth: Float): Pair<TextPaint, String> {
        if (base.measureText(text) <= maxWidth) return base to text
        val p = TextPaint(base)
        var size = base.textSize
        while (p.measureText(text) > maxWidth && size > MIN_FIT_SIZE) {
            size -= 0.5f
            p.textSize = size
        }
        if (p.measureText(text) <= maxWidth) return p to text
        val truncated = TextUtils.ellipsize(text, p, maxWidth, TextUtils.TruncateAt.END).toString()
        return p to truncated
    }

    /** Builds a wrapped, measured text block. Width is fixed at [width]; height is exact. */
    private fun buildLayout(text: String, p: TextPaint, width: Float, alignment: Layout.Alignment): StaticLayout {
        val safeWidth = width.toInt().coerceAtLeast(1)
        val safeText = text.ifBlank { " " }
        return StaticLayout.Builder.obtain(safeText, 0, safeText.length, p, safeWidth)
            .setAlignment(alignment)
            .setLineSpacing(0f, 1.05f)
            .setIncludePad(false)
            .build()
    }

    private fun drawLayout(canvas: Canvas, layout: StaticLayout, x: Float, topY: Float) {
        canvas.save()
        canvas.translate(x, topY)
        layout.draw(canvas)
        canvas.restore()
    }

    private fun hLine(canvas: Canvas, y: Float, color: Int = COLOR_THIN_LINE, strokeWidth: Float = 1f) {
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color; this.strokeWidth = strokeWidth }
        canvas.drawLine(CONTENT_LEFT, y, CONTENT_RIGHT, y, p)
    }

    // ── Main render ─────────────────────────────────────────────────

    private fun writePdf(data: ShareBillData, out: FileOutputStream) {
        val document = PdfDocument()

        var pageNumber = 0
        var currentPage: PdfDocument.Page? = null
        var canvas: Canvas
        var y = 0f

        fun finishCurrentPage() {
            currentPage?.let { document.finishPage(it) }
        }

        // Starts a fresh page, draws the (repeated) shop header on it, and returns
        // the canvas to draw on. Also stamps a small page-number footer immediately,
        // since it lives in the outer margin and can never collide with content.
        fun newPage(): Canvas {
            finishCurrentPage()
            pageNumber += 1
            val info = PdfDocument.PageInfo.Builder(PAGE_WIDTH.toInt(), PAGE_HEIGHT.toInt(), pageNumber).create()
            val page = document.startPage(info)
            currentPage = page
            val c = page.canvas
            c.drawColor(Color.WHITE)
            drawPageNumber(c, pageNumber)
            y = drawShopHeader(c, data)
            return c
        }

        canvas = newPage()

        // Bill info (bill number / date / time) only on the first page, directly
        // below the shop header and above the product table.
        y = drawBillInfo(canvas, y, data)

        // Product table header (repeated on every page that has table rows).
        y = drawTableHeader(canvas, y)

        // ── Product rows, with automatic page breaks ────────────────
        for ((index, item) in data.items.withIndex()) {
            val namePaint = textPaint(SIZE_ITEM, bold = false)
            val nameLayout = buildLayout(item.name, namePaint, NAME_W, Layout.Alignment.ALIGN_NORMAL)
            val valuePaint = textPaint(SIZE_ITEM)
            val rowContentH = maxOf(nameLayout.height.toFloat(), lineHeight(valuePaint))
            val rowH = rowContentH + ROW_PAD_TOP + ROW_PAD_BOTTOM

            if (y + rowH > BOTTOM_LIMIT) {
                canvas = newPage()
                y = drawTableHeader(canvas, y)
            }

            y = drawItemRow(canvas, y, item, nameLayout, rowH, index)
        }

        hLine(canvas, y, COLOR_THIN_LINE)
        y += 14f

        // ── Totals (subtotal / discount / grand total) ───────────────
        // Drawn only after the complete product table, and only once there is
        // room for the whole block — otherwise it moves to a fresh page rather
        // than splitting or overlapping the table.
        val totalsH = measureTotalsHeight(data)
        if (y + totalsH > BOTTOM_LIMIT) {
            canvas = newPage()
        }
        y = drawTotals(canvas, y, data)

        // ── Thank-you footer ──────────────────────────────────────────
        val footerPaint = textPaint(SIZE_FOOTER, italic = true, color = COLOR_FOOTER_TEXT)
        val footerLayout = buildLayout(data.thankYou, footerPaint, CONTENT_WIDTH, Layout.Alignment.ALIGN_CENTER)
        val footerH = footerLayout.height.toFloat() + 16f
        if (y + footerH > BOTTOM_LIMIT) {
            canvas = newPage()
        }
        y += 10f
        drawLayout(canvas, footerLayout, CONTENT_LEFT, y)

        finishCurrentPage()
        document.writeTo(out)
        document.close()
    }

    // ── Draw: page number ──────────────────────────────────────────

    private fun drawPageNumber(canvas: Canvas, pageNumber: Int) {
        val p = textPaint(SIZE_FOOTER, color = Color.GRAY, align = Paint.Align.RIGHT)
        canvas.drawText("Page $pageNumber", CONTENT_RIGHT, PAGE_HEIGHT - 18f, p)
    }

    // ── Draw: shop header (repeated on every page) ────────────────

    private fun drawShopHeader(canvas: Canvas, data: ShareBillData): Float {
        var y = MARGIN

        // NOTE: paints handed to StaticLayout must keep Paint.Align.LEFT (the
        // default). StaticLayout positions each line itself based on the
        // Layout.Alignment argument below; if the paint's own textAlign is
        // also set to CENTER/RIGHT, the offset gets applied a second time on
        // top of StaticLayout's own centering, shifting text off the page.
        val pShop = textPaint(SIZE_SHOP_NAME, bold = true)
        val shopLayout = buildLayout(data.shopName, pShop, CONTENT_WIDTH, Layout.Alignment.ALIGN_CENTER)
        drawLayout(canvas, shopLayout, CONTENT_LEFT, y)
        y += shopLayout.height.toFloat()

        if (data.showAddress && data.address.isNotBlank()) {
            val pAddr = textPaint(SIZE_SUBHEADER)
            val addrLayout = buildLayout(data.address, pAddr, CONTENT_WIDTH, Layout.Alignment.ALIGN_CENTER)
            drawLayout(canvas, addrLayout, CONTENT_LEFT, y)
            y += addrLayout.height.toFloat()
        }

        if (data.phone.isNotBlank()) {
            val pPhone = textPaint(SIZE_SUBHEADER)
            val phoneLayout = buildLayout("Phone: ${data.phone}", pPhone, CONTENT_WIDTH, Layout.Alignment.ALIGN_CENTER)
            drawLayout(canvas, phoneLayout, CONTENT_LEFT, y)
            y += phoneLayout.height.toFloat()
        }

        y += 8f
        hLine(canvas, y, COLOR_GRAY_LINE, 1.2f)
        y += 12f
        return y
    }

    // ── Draw: bill info (first page only) ──────────────────────────

    private fun drawBillInfo(canvas: Canvas, startY: Float, data: ShareBillData): Float {
        var y = startY
        val pBold = textPaint(SIZE_BILL_INFO, bold = true)
        val pNorm = textPaint(SIZE_BILL_INFO)

        val billLayout = buildLayout("Bill No: ${data.billNumber}", pBold, CONTENT_WIDTH, Layout.Alignment.ALIGN_NORMAL)
        drawLayout(canvas, billLayout, CONTENT_LEFT, y)
        y += billLayout.height.toFloat() + 3f

        drawSingleLine(canvas, "Date: ${data.date}", pNorm, CONTENT_LEFT, y)
        y += lineHeight(pNorm) + 2f

        drawSingleLine(canvas, "Time: ${data.time}", pNorm, CONTENT_LEFT, y)
        y += lineHeight(pNorm) + 10f

        hLine(canvas, y)
        y += 12f
        return y
    }

    // ── Draw: product table header row (repeated on every page) ────

    private fun drawTableHeader(canvas: Canvas, startY: Float): Float {
        val p = textPaint(SIZE_TABLE_HEADER, bold = true)
        val rowH = lineHeight(p) + 10f

        val bg = Paint().apply { color = COLOR_HEADER_BG }
        canvas.drawRect(CONTENT_LEFT, startY, CONTENT_RIGHT, startY + rowH, bg)

        val topY = startY + (rowH - lineHeight(p)) / 2f
        p.textAlign = Paint.Align.LEFT
        drawSingleLine(canvas, "Product Name", p, NAME_LEFT, topY)
        p.textAlign = Paint.Align.CENTER
        drawSingleLine(canvas, "Qty", p, (QTY_LEFT + QTY_RIGHT) / 2f, topY)
        p.textAlign = Paint.Align.RIGHT
        drawSingleLine(canvas, "Price", p, PRICE_RIGHT, topY)
        drawSingleLine(canvas, "Amount", p, AMOUNT_RIGHT, topY)

        var y = startY + rowH
        hLine(canvas, y)
        y += 4f
        return y
    }

    // ── Draw: one product row ───────────────────────────────────────

    private fun drawItemRow(
        canvas: Canvas,
        startY: Float,
        item: BillLine,
        nameLayout: StaticLayout,
        rowH: Float,
        index: Int
    ): Float {
        if (index % 2 == 1) {
            val bg = Paint().apply { color = COLOR_ROW_ALT_BG }
            canvas.drawRect(CONTENT_LEFT, startY, CONTENT_RIGHT, startY + rowH, bg)
        }

        val topY = startY + ROW_PAD_TOP

        // Product name — wraps within its own column, never crosses into Qty.
        drawLayout(canvas, nameLayout, NAME_LEFT, topY)

        // Qty / Price / Amount — each shrinks-to-fit then ellipsizes as a last
        // resort, so a very large quantity/price/amount can never spill into
        // the neighboring column.
        val basePaint = textPaint(SIZE_ITEM)

        val (qtyPaint, qtyText) = fitSingleLine(item.quantity, TextPaint(basePaint).apply { textAlign = Paint.Align.CENTER }, QTY_W)
        drawSingleLine(canvas, qtyText, qtyPaint, (QTY_LEFT + QTY_RIGHT) / 2f, topY)

        val priceStr = Money.paiseToDisplay(item.ratePaise)
        val (pricePaint, priceText) = fitSingleLine(priceStr, TextPaint(basePaint).apply { textAlign = Paint.Align.RIGHT }, PRICE_W)
        drawSingleLine(canvas, priceText, pricePaint, PRICE_RIGHT, topY)

        val amountStr = Money.paiseToDisplay(item.amountPaise)
        val (amountPaint, amountText) = fitSingleLine(amountStr, TextPaint(basePaint).apply { textAlign = Paint.Align.RIGHT }, AMOUNT_W)
        drawSingleLine(canvas, amountText, amountPaint, AMOUNT_RIGHT, topY)

        return startY + rowH
    }

    // ── Totals ───────────────────────────────────────────────────────

    private fun measureTotalsHeight(data: ShareBillData): Float {
        val pLabel = textPaint(SIZE_TOTALS)
        val pGrand = textPaint(SIZE_GRAND_TOTAL, bold = true)
        val lineH = lineHeight(pLabel) + 8f
        val grandH = lineHeight(pGrand) + 16f

        var h = 4f
        h += lineH // subtotal
        if (data.discountPaise > 0) h += lineH // discount
        h += 4f // gap before the grand-total band (matches drawTotals)
        h += grandH
        return h
    }

    private fun drawTotals(canvas: Canvas, startY: Float, data: ShareBillData): Float {
        var y = startY + 4f
        val pLabel = textPaint(SIZE_TOTALS)
        val pLabelRight = textPaint(SIZE_TOTALS, align = Paint.Align.RIGHT)
        val lineH = lineHeight(pLabel) + 8f

        drawSingleLine(canvas, "Subtotal", pLabel, CONTENT_LEFT, y)
        drawSingleLine(canvas, Money.paiseToDisplay(data.subtotalPaise), pLabelRight, CONTENT_RIGHT, y)
        y += lineH

        if (data.discountPaise > 0) {
            drawSingleLine(canvas, "Discount", pLabel, CONTENT_LEFT, y)
            drawSingleLine(canvas, "-${Money.paiseToDisplay(data.discountPaise)}", pLabelRight, CONTENT_RIGHT, y)
            y += lineH
        }

        // Grand total, visually separated with its own background band.
        val pGrandLabel = textPaint(SIZE_GRAND_TOTAL, bold = true)
        val pGrandValue = textPaint(SIZE_GRAND_TOTAL, bold = true, color = COLOR_TOTAL_VALUE, align = Paint.Align.RIGHT)
        val grandLineH = lineHeight(pGrandLabel)
        val grandRowH = grandLineH + 16f

        y += 4f
        val bg = Paint().apply { color = COLOR_TOTAL_BG }
        canvas.drawRect(CONTENT_LEFT, y, CONTENT_RIGHT, y + grandRowH, bg)
        val textTopY = y + (grandRowH - grandLineH) / 2f
        drawSingleLine(canvas, "TOTAL", pGrandLabel, CONTENT_LEFT + 6f, textTopY)
        drawSingleLine(canvas, Money.paiseToDisplay(data.totalPaise), pGrandValue, CONTENT_RIGHT - 6f, textTopY)
        y += grandRowH

        return y
    }
}
