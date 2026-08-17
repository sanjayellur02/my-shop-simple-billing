package com.grocery.billing.share

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.grocery.billing.print.ReceiptViewBuilder
import java.io.File
import java.io.FileOutputStream

/**
 * Renders a bill as a PDF using [ReceiptViewBuilder] and exposes it
 * through a FileProvider content:// URI so other apps (e.g. WhatsApp) can
 * attach it as a document.
 *
 * Multi-page support: when the bill contains many products, content is
 * automatically split across pages with the shop header on every page.
 */
object BillPdf {

    private const val PAGE_WIDTH = 595   // A4 width in points
    private const val PAGE_HEIGHT = 842  // A4 height in points
    private const val MARGIN = 36f

    fun generateShareUri(context: Context, data: ShareBillData): Uri? {
        val dir = File(context.cacheDir, "shared_bills").apply { mkdirs() }
        val safeNumber = data.billNumber.replace(Regex("[^A-Za-z0-9_-]"), "_").ifBlank { "bill" }
        val file = File(dir, "bill_$safeNumber.pdf")
        return try {
            FileOutputStream(file).use { out -> writePdf(context, data, out) }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun writePdf(context: Context, data: ShareBillData, out: FileOutputStream) {
        val document = PdfDocument()

        // ── Constants ──────────────────────────────────────────────────
        val contentWidth = PAGE_WIDTH - MARGIN * 2
        val contentHeight = PAGE_HEIGHT - MARGIN * 2

        // Receipt bitmap width is fixed at 1152 px (see ReceiptViewBuilder).
        // Scale that to fit the A4 content width.
        val receiptWidth = 1152
        val scale = contentWidth / receiptWidth.toFloat()

        // ── Measure how many items fit per page ────────────────────────
        // First page: header + bill info + items + totals + footer
        // Continuation pages: header + items + footer
        // Last page: items + totals + footer

        // Header height (shop name, optional address, phone, separator)
        val headerH = measureHeaderPx(data) * scale
        // Bill info height (bill number, date, time, separator)
        val billInfoH = measureBillInfoPx() * scale
        // Footer height (thank you)
        val footerH = measureFooterPx(data.thankYou) * scale
        // Per-item height (approximate, based on longest name)
        val perItemH = measureItemRowPx(data) * scale
        // Totals height (subtotal + discount + total)
        val totalsH = measureTotalsPx(data) * scale

        // First page space for items
        val firstPageAvailable = contentHeight - headerH - billInfoH - footerH - totalsH - 40f
        val firstPageItems = (firstPageAvailable / perItemH).toInt().coerceAtLeast(1)

        // Continuation page space for items
        val contPageAvailable = contentHeight - headerH - footerH - 40f
        val contPageItems = (contPageAvailable / perItemH).toInt().coerceAtLeast(1)

        // ── Split items across pages ───────────────────────────────────
        val pages = mutableListOf<List<com.grocery.billing.print.BillLine>>()
        var remaining = data.items.toList()

        if (remaining.size <= firstPageItems) {
            // Everything fits on one page
            pages.add(remaining)
        } else {
            // First page
            pages.add(remaining.take(firstPageItems))
            remaining = remaining.drop(firstPageItems)
            // Continuation pages
            while (remaining.size > contPageItems) {
                pages.add(remaining.take(contPageItems))
                remaining = remaining.drop(contPageItems)
            }
            // Last page
            if (remaining.isNotEmpty()) pages.add(remaining)
        }

        val totalPages = pages.size

        // ── Render each page ───────────────────────────────────────────
        for ((pageIndex, pageItems) in pages.withIndex()) {
            val isLast = pageIndex == totalPages - 1

            // Build page data
            val pageData = if (isLast) {
                data
            } else {
                data.copy(items = pageItems)
            }

            // Render this page as a bitmap
            val bitmap = ReceiptViewBuilder.renderReceiptPage(
                context = context,
                shopName = data.shopName,
                address = data.address,
                phone = data.phone,
                showAddress = data.showAddress,
                billNumber = data.billNumber,
                date = data.date,
                time = data.time,
                items = if (isLast) data.items else pageItems,
                subtotalPaise = if (isLast) data.subtotalPaise else 0L,
                discountPaise = if (isLast) data.discountPaise else 0L,
                totalPaise = if (isLast) data.totalPaise else 0L,
                thankYou = data.thankYou,
                pageIndex = pageIndex,
                totalPages = totalPages,
                pageHeight = (bitmapHeight(pageItems, isLast, data) * scale).toInt().coerceAtLeast(PAGE_HEIGHT - (MARGIN * 2).toInt())
            )

            val page = document.startPage(
                PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageIndex + 1).create()
            )
            val canvas = page.canvas
            canvas.drawColor(android.graphics.Color.WHITE)

            val srcH = bitmap.height.toFloat()
            val dstH = srcH * scale
            canvas.drawBitmap(
                bitmap,
                null,
                android.graphics.RectF(MARGIN, MARGIN, MARGIN + bitmap.width * scale, MARGIN + dstH),
                null
            )
            document.finishPage(page)
        }

        document.writeTo(out)
        document.close()
    }

    // ── Measurement helpers (in receipt px, before scale) ─────────────

    private fun measureHeaderPx(data: ShareBillData): Float {
        var h = 48f // top margin
        h += 52f * 1.2f  // shop name line height
        if (data.showAddress && data.address.isNotBlank()) h += 36f * 1.2f
        if (data.phone.isNotBlank()) h += 36f * 1.2f
        h += 24f // gap + separator
        return h
    }

    private fun measureBillInfoPx(): Float {
        return 36f * 1.2f * 3 + 24f // 3 lines + gap
    }

    private fun measureItemRowPx(data: ShareBillData): Float {
        val nameH = 34f * 1.2f
        val valH = 32f * 1.2f
        return nameH + valH + 16f
    }

    private fun measureTotalsPx(data: ShareBillData): Float {
        val lineH = 34f * 1.2f
        var h = 24f // gap
        h += lineH + 10f // subtotal
        if (data.discountPaise > 0) h += lineH + 10f // discount
        h += 40f * 1.2f + 10f // total
        h += 24f // gap
        return h
    }

    private fun measureFooterPx(thankYou: String): Float {
        return 30f * 1.2f + 24f
    }

    private fun bitmapHeight(
        items: List<com.grocery.billing.print.BillLine>,
        isLast: Boolean,
        data: ShareBillData
    ): Float {
        var h = 0f
        h += measureHeaderPx(data)
        if (isLast) {
            h += measureBillInfoPx()
        }
        h += items.size * measureItemRowPx(data)
        if (isLast) {
            h += measureTotalsPx(data)
        }
        h += measureFooterPx(data.thankYou)
        h += 48f // bottom padding
        return h
    }
}
