package com.grocery.billing.print

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.grocery.billing.money.Money
import java.io.FileOutputStream

/**
 * Prints a bill through the Android PrintManager. This works with the Android
 * printing service of compatible Bluetooth/USB/network receipt printers and
 * also produces a PDF via the normal Android print option.
 */
object BillPrinter {

    fun print(
        context: Context,
        shopName: String,
        address: String,
        phone: String,
        showAddress: Boolean,
        billNumber: String,
        date: String,
        time: String,
        items: List<BillLine>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long,
        thankYou: String
    ) {
        val bitmap = ReceiptViewBuilder.renderReceiptBitmap(
            context, shopName, address, phone, showAddress,
            billNumber, date, time, items, subtotalPaise, discountPaise, totalPaise, thankYou
        )
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        printManager.print(
            "Bill $billNumber",
            ReceiptPrintAdapter(bitmap),
            PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                .build()
        )
    }
}

data class BillLine(val name: String, val quantity: String, val ratePaise: Long, val amountPaise: Long)

/**
 * Builds a clean, professional receipt layout by drawing directly on a Bitmap.
 * Used for both thermal printing (single tall bitmap) and PDF generation
 * (via [renderReceiptPage] for multi-page support).
 */
object ReceiptViewBuilder {

    // ── Layout constants ──────────────────────────────────────────────

    private const val RECEIPT_WIDTH_PX = 1152        // 384 mm at 96 dpi
    private const val MARGIN = 48f                    // 16 mm at 96 dpi
    private const val CONTENT_LEFT = MARGIN
    private const val CONTENT_RIGHT = RECEIPT_WIDTH_PX - MARGIN

    private const val TABLE_GAP = 20                  // space between name/value columns

    private const val TEXT_SIZE_SHOP = 52f
    private const val TEXT_SIZE_HEADER = 36f
    private const val TEXT_SIZE_TABLE_HEADER = 32f
    private const val TEXT_SIZE_ITEM = 32f
    private const val TEXT_SIZE_ITEM_NAME = 34f
    private const val TEXT_SIZE_TOTALS = 34f
    private const val TEXT_SIZE_GRAND = 40f
    private const val TEXT_SIZE_FOOTER = 30f

    // ── Paints ────────────────────────────────────────────────────────

    private fun paint(size: Float, bold: Boolean = false, color: Int = Color.BLACK): Paint {
        val tf = if (bold) Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                 else Typeface.DEFAULT
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size
            typeface = tf
            this.color = color
        }
    }

    // ── Text helpers ──────────────────────────────────────────────────

    private fun textHeight(p: Paint): Float {
        val b = android.graphics.Rect()
        p.getTextBounds("Ay", 0, 2, b)
        return (b.height() * 1.15f).coerceAtLeast(p.textSize)
    }

    private fun drawCentered(canvas: Canvas, text: String, p: Paint, cx: Float, y: Float) {
        canvas.drawText(text, cx - p.measureText(text) / 2f, y, p)
    }

    private fun drawLeft(canvas: Canvas, text: String, p: Paint, x: Float, y: Float) {
        canvas.drawText(text, x, y, p)
    }

    private fun drawRight(canvas: Canvas, text: String, p: Paint, x: Float, y: Float) {
        canvas.drawText(text, x - p.measureText(text), y, p)
    }

    private fun drawLine(canvas: Canvas, y: Float) {
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#888888")
            strokeWidth = 2f
        }
        canvas.drawLine(CONTENT_LEFT.toFloat(), y, CONTENT_RIGHT.toFloat(), y, linePaint)
    }

    /** Wraps text into lines that fit within [maxWidth]. */
    private fun wrapText(text: String, p: Paint, maxWidth: Float): List<String> {
        if (p.measureText(text) <= maxWidth) return listOf(text)
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var current = ""
        for (w in words) {
            val test = if (current.isEmpty()) w else "$current $w"
            if (p.measureText(test) > maxWidth && current.isNotEmpty()) {
                lines.add(current)
                current = w
            } else {
                current = test
            }
        }
        if (current.isNotEmpty()) lines.add(current)
        return lines.ifEmpty { listOf(text) }
    }

    // ── Public: full receipt bitmap (for printer) ─────────────────────

    fun renderReceiptBitmap(
        context: Context,
        shopName: String,
        address: String,
        phone: String,
        showAddress: Boolean,
        billNumber: String,
        date: String,
        time: String,
        items: List<BillLine>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long,
        thankYou: String
    ): Bitmap {
        // Measure full height
        var y = 0f
        y += measureHeader(shopName, address, phone, showAddress)
        y += measureBillInfo(billNumber, date, time)
        y += measureItems(items)
        y += measureTotals(items, subtotalPaise, discountPaise, totalPaise)
        y += measureFooter(thankYou)
        y += MARGIN // bottom padding

        val totalH = y.toInt().coerceAtLeast(200)
        val bitmap = Bitmap.createBitmap(RECEIPT_WIDTH_PX, totalH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw everything
        drawHeader(canvas, shopName, address, phone, showAddress)
        drawBillInfo(canvas, billNumber, date, time)
        drawItemTable(canvas, items)
        drawTotals(canvas, items, subtotalPaise, discountPaise, totalPaise)
        drawFooter(canvas, thankYou)

        return bitmap
    }

    // ── Public: single page bitmap (for PDF) ─────────────────────────

    fun renderReceiptPage(
        context: Context,
        shopName: String,
        address: String,
        phone: String,
        showAddress: Boolean,
        billNumber: String,
        date: String,
        time: String,
        items: List<BillLine>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long,
        thankYou: String,
        pageIndex: Int,
        totalPages: Int,
        pageHeight: Int
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(RECEIPT_WIDTH_PX, pageHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Header (every page)
        drawHeader(canvas, shopName, address, phone, showAddress)

        // Bill info + separator (only first page)
        if (pageIndex == 0) {
            drawBillInfo(canvas, billNumber, date, time)
        }

        // Items or totals on last page
        if (pageIndex == totalPages - 1) {
            drawItemTable(canvas, items)
            drawTotals(canvas, items, subtotalPaise, discountPaise, totalPaise)
            drawFooter(canvas, thankYou)
        } else {
            // This is a continuation page — draw items only
            val headerH = measureHeader(shopName, address, phone, showAddress)
            drawItemTableFrom(canvas, items, headerH)
        }

        // Page number (if multi-page)
        if (totalPages > 1) {
            val pNum = paint(TEXT_SIZE_FOOTER, false, Color.GRAY)
            drawCentered(canvas, "Page ${pageIndex + 1} of $totalPages", pNum,
                RECEIPT_WIDTH_PX / 2f, pageHeight - 16f)
        }

        return bitmap
    }

    // ── Measure helpers ───────────────────────────────────────────────

    private fun measureHeader(
        shopName: String, address: String, phone: String, showAddress: Boolean
    ): Float {
        val pH = paint(TEXT_SIZE_SHOP, true)
        var y = MARGIN + textHeight(pH)
        if (showAddress && address.isNotBlank()) {
            y += textHeight(paint(TEXT_SIZE_HEADER))
        }
        if (phone.isNotBlank()) {
            y += textHeight(paint(TEXT_SIZE_HEADER))
        }
        y += 24f // gap before separator
        return y
    }

    private fun measureBillInfo(billNumber: String, date: String, time: String): Float {
        val p = paint(TEXT_SIZE_HEADER)
        val h = textHeight(p)
        return h * 3 + 24f // 3 lines + gap
    }

    private fun measureItems(items: List<BillLine>): Float {
        val pName = paint(TEXT_SIZE_ITEM_NAME, true)
        val pVal = paint(TEXT_SIZE_ITEM)
        val hName = textHeight(pName)
        val hVal = textHeight(pVal)
        val nameW = CONTENT_RIGHT - CONTENT_LEFT - TABLE_GAP - 160f
        var totalH = 0f
        for (item in items) {
            val lines = wrapText(item.name, pName, nameW)
            totalH += hName * lines.size + hVal + 16f
        }
        return totalH
    }

    private fun measureTotals(
        items: List<BillLine>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long
    ): Float {
        val p = paint(TEXT_SIZE_TOTALS)
        val h = textHeight(p)
        var totalH = 24f // gap
        totalH += h + 10f // subtotal
        if (discountPaise > 0) totalH += h + 10f // discount
        totalH += textHeight(paint(TEXT_SIZE_GRAND, true)) + 10f // total
        totalH += 24f // gap
        return totalH
    }

    private fun measureFooter(thankYou: String): Float {
        val p = paint(TEXT_SIZE_FOOTER)
        return textHeight(p) + 24f
    }

    // ── Draw: header ──────────────────────────────────────────────────

    private fun drawHeader(
        canvas: Canvas, shopName: String, address: String, phone: String, showAddress: Boolean
    ) {
        val cx = RECEIPT_WIDTH_PX / 2f
        var y = MARGIN

        val pShop = paint(TEXT_SIZE_SHOP, true)
        drawCentered(canvas, shopName, pShop, cx, y + textHeight(pShop))
        y += textHeight(pShop)

        if (showAddress && address.isNotBlank()) {
            val pAddr = paint(TEXT_SIZE_HEADER)
            drawCentered(canvas, address, pAddr, cx, y + textHeight(pAddr))
            y += textHeight(pAddr)
        }
        if (phone.isNotBlank()) {
            val pPh = paint(TEXT_SIZE_HEADER)
            drawCentered(canvas, "Phone: $phone", pPh, cx, y + textHeight(pPh))
            y += textHeight(pPh)
        }

        y += 12f
        drawLine(canvas, y)
        y += 12f
    }

    // ── Draw: bill info ───────────────────────────────────────────────

    private fun drawBillInfo(canvas: Canvas, billNumber: String, date: String, time: String) {
        val pBold = paint(TEXT_SIZE_HEADER, true)
        val pNorm = paint(TEXT_SIZE_HEADER)
        var y = 12f

        drawLeft(canvas, "Bill No: $billNumber", pBold, CONTENT_LEFT.toFloat(), y + textHeight(pBold))
        y += textHeight(pBold) + 6f

        drawLeft(canvas, "Date: $date", pNorm, CONTENT_LEFT.toFloat(), y + textHeight(pNorm))
        y += textHeight(pNorm) + 2f

        drawLeft(canvas, "Time: $time", pNorm, CONTENT_LEFT.toFloat(), y + textHeight(pNorm))
        y += textHeight(pNorm) + 12f

        drawLine(canvas, y)
        y += 12f
    }

    // ── Draw: item table ──────────────────────────────────────────────

    private fun drawItemTable(canvas: Canvas, items: List<BillLine>) {
        drawItemTableFrom(canvas, items, 0f)
    }

    /**
     * Draws the item table starting at [startY] in the canvas coordinate space.
     */
    private fun drawItemTableFrom(canvas: Canvas, items: List<BillLine>, startY: Float) {
        val pHeader = paint(TEXT_SIZE_TABLE_HEADER, true)
        val pName = paint(TEXT_SIZE_ITEM_NAME, true)
        val pVal = paint(TEXT_SIZE_ITEM)
        val hHeader = textHeight(pHeader)
        val hName = textHeight(pName)
        val hVal = textHeight(pVal)

        val nameW = CONTENT_RIGHT - CONTENT_LEFT - TABLE_GAP - 160f
        val colName = CONTENT_LEFT.toFloat()
        val colQty = CONTENT_RIGHT - 160f
        val colRate = CONTENT_RIGHT - 110f
        val colAmt = CONTENT_RIGHT.toFloat()

        var y = startY

        // Table header row with light background
        val headerBg = Paint().apply { color = Color.parseColor("#F0F0F0") }
        canvas.drawRect(colName, y, colAmt, y + hHeader + 10f, headerBg)
        drawLeft(canvas, "Item", pHeader, colName, y + hHeader)
        drawLeft(canvas, "Qty", pHeader, colQty, y + hHeader)
        drawLeft(canvas, "Rate", pHeader, colRate, y + hHeader)
        drawRight(canvas, "Amount", pHeader, colAmt, y + hHeader)
        y += hHeader + 10f

        // Thin line under header
        val thinLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#CCCCCC"); strokeWidth = 1f
        }
        canvas.drawLine(colName, y, colAmt, y, thinLine)
        y += 6f

        // Item rows
        val evenBg = Paint().apply { color = Color.parseColor("#FAFAFA") }
        for ((i, item) in items.withIndex()) {
            val nameLines = wrapText(item.name, pName, nameW)
            val rowH = hName * nameLines.size + hVal + 16f

            // Alternating row background
            if (i % 2 == 1) {
                canvas.drawRect(colName, y, colAmt, y + rowH, evenBg)
            }

            // Product name (potentially wrapped)
            for ((li, line) in nameLines.withIndex()) {
                drawLeft(canvas, line, pName, colName, y + hName * (li + 1))
            }

            // Qty, Rate, Amount
            val valY = y + hName * nameLines.size + hVal
            drawLeft(canvas, item.quantity, pVal, colQty, valY)
            drawLeft(canvas, Money.paiseToDisplay(item.ratePaise), pVal, colRate, valY)
            drawRight(canvas, Money.paiseToDisplay(item.amountPaise), pVal, colAmt, valY)

            y += rowH
        }

        // Line under items
        canvas.drawLine(colName, y, colAmt, y, thinLine)
        y += 12f
    }

    // ── Draw: totals ──────────────────────────────────────────────────

    private fun drawTotals(
        canvas: Canvas,
        items: List<BillLine>,
        subtotalPaise: Long,
        discountPaise: Long,
        totalPaise: Long
    ) {
        val pLabel = paint(TEXT_SIZE_TOTALS)
        val pVal = paint(TEXT_SIZE_TOTALS)
        val pTotalLabel = paint(TEXT_SIZE_GRAND, true)
        val pTotalVal = paint(TEXT_SIZE_GRAND, true, Color.parseColor("#1B5E20"))
        val hLabel = textHeight(pLabel)
        val hTotal = textHeight(pTotalLabel)

        var y = 12f

        // Subtotal
        drawLeft(canvas, "Subtotal", pLabel, CONTENT_LEFT.toFloat(), y + hLabel)
        drawRight(canvas, Money.paiseToDisplay(subtotalPaise), pVal, CONTENT_RIGHT.toFloat(), y + hLabel)
        y += hLabel + 10f

        // Discount
        if (discountPaise > 0) {
            drawLeft(canvas, "Discount", pLabel, CONTENT_LEFT.toFloat(), y + hLabel)
            drawRight(canvas, "-${Money.paiseToDisplay(discountPaise)}", pVal, CONTENT_RIGHT.toFloat(), y + hLabel)
            y += hLabel + 10f
        }

        // Grand total (with subtle background)
        val totalBg = Paint().apply { color = Color.parseColor("#F0F8F0") }
        canvas.drawRect(CONTENT_LEFT.toFloat(), y, CONTENT_RIGHT.toFloat(), y + hTotal + 14f, totalBg)
        drawLeft(canvas, "TOTAL", pTotalLabel, CONTENT_LEFT.toFloat(), y + hTotal)
        drawRight(canvas, Money.paiseToDisplay(totalPaise), pTotalVal, CONTENT_RIGHT.toFloat(), y + hTotal)
        y += hTotal + 14f + 12f

        // Line under totals
        drawLine(canvas, y)
        y += 12f
    }

    // ── Draw: footer ──────────────────────────────────────────────────

    private fun drawFooter(canvas: Canvas, thankYou: String) {
        val p = paint(TEXT_SIZE_FOOTER, false, Color.parseColor("#666666"))
        val cx = RECEIPT_WIDTH_PX / 2f
        val y = 12f + textHeight(p)
        drawCentered(canvas, thankYou, p, cx, y)
    }
}

private class ReceiptPrintAdapter(
    private val bitmap: Bitmap
) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        if (cancellationSignal.isCanceled) {
            callback.onLayoutCancelled()
            return
        }
        val info = PrintDocumentInfo.Builder("bill")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(1)
            .build()
        callback.onLayoutFinished(info, true)
    }

    override fun onWrite(
        pages: Array<out PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal,
        callback: WriteResultCallback
    ) {
        if (cancellationSignal.isCanceled) {
            callback.onWriteCancelled()
            return
        }
        try {
            FileOutputStream(destination.fileDescriptor).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: Exception) {
            callback.onWriteFailed(e.message)
        }
    }
}
