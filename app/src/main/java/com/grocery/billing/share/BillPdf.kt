package com.grocery.billing.share

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import com.grocery.billing.print.BillLine
import com.grocery.billing.print.ReceiptViewBuilder
import java.io.File
import java.io.FileOutputStream

/**
 * Renders a bill as a PDF (same layout used for printing) and exposes it
 * through a FileProvider content:// URI so other apps (e.g. WhatsApp) can
 * attach it as a document.
 */
object BillPdf {

    private const val PAGE_WIDTH = 595 // A4 width in points
    private const val PAGE_HEIGHT = 842 // A4 height in points
    private const val MARGIN = 36f

    fun generateShareUri(context: Context, data: ShareBillData): Uri? {
        val view = ReceiptViewBuilder.build(
            context = context,
            shopName = data.shopName,
            address = data.address,
            phone = data.phone,
            showAddress = data.showAddress,
            billNumber = data.billNumber,
            date = data.date,
            time = data.time,
            items = data.items,
            subtotalPaise = data.subtotalPaise,
            discountPaise = data.discountPaise,
            totalPaise = data.totalPaise,
            thankYou = data.thankYou
        )
        val bitmap = renderToBitmap(context, view) ?: return null
        val dir = File(context.cacheDir, "shared_bills").apply { mkdirs() }
        val safeNumber = data.billNumber.replace(Regex("[^A-Za-z0-9_-]"), "_").ifBlank { "bill" }
        val file = File(dir, "bill_$safeNumber.pdf")
        return try {
            FileOutputStream(file).use { out -> writePdf(bitmap, out) }
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun renderToBitmap(context: Context, view: View): Bitmap? {
        return try {
            val density = context.resources.displayMetrics.density
            val widthPx = (384 * density).toInt().coerceAtLeast(384)
            view.measure(
                View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            val width = view.measuredWidth.coerceAtLeast(1)
            val height = view.measuredHeight.coerceAtLeast(1)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)
            view.draw(canvas)
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Writes the receipt as a multi-page A4 PDF. When the bill is longer than
     * one page the remaining content flows onto additional pages, so nothing
     * is cut off.
     */
    private fun writePdf(bitmap: Bitmap, out: FileOutputStream) {
        val contentWidth = PAGE_WIDTH - MARGIN * 2
        val contentHeight = PAGE_HEIGHT - MARGIN * 2
        val scale = contentWidth / bitmap.width.toFloat()
        val scaledWidth = bitmap.width * scale
        val bitmapRowsPerPage = (contentHeight / scale).toInt().coerceAtLeast(1)

        val document = PdfDocument()
        var srcY = 0
        var pageNumber = 1
        while (srcY < bitmap.height) {
            val sliceHeight = minOf(bitmapRowsPerPage, bitmap.height - srcY)
            val page = document.startPage(
                PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
            )
            val canvas = page.canvas
            canvas.drawColor(Color.WHITE)
            val src = Rect(0, srcY, bitmap.width, srcY + sliceHeight)
            val dst = RectF(
                MARGIN,
                MARGIN,
                MARGIN + scaledWidth,
                MARGIN + sliceHeight * scale
            )
            canvas.drawBitmap(bitmap, src, dst, null)
            document.finishPage(page)
            srcY += sliceHeight
            pageNumber++
        }
        document.writeTo(out)
        document.close()
    }
}
