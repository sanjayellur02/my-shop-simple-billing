package com.grocery.billing.print

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
        val view = ReceiptViewBuilder.build(
            context, shopName, address, phone, showAddress,
            billNumber, date, time, items, subtotalPaise, discountPaise, totalPaise, thankYou
        )
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        printManager.print(
            "Bill $billNumber",
            ReceiptPrintAdapter(context, view),
            PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
                .build()
        )
    }
}

data class BillLine(val name: String, val quantity: String, val ratePaise: Long, val amountPaise: Long)

/**
 * Builds a clean receipt layout as a classic Android View hierarchy.
 */
object ReceiptViewBuilder {

    @SuppressLint("SetTextI18n")
    fun build(
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
    ): View {
        val density = context.resources.displayMetrics.density
        fun dp(v: Int): Int = (v * density).toInt()

        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
            setBackgroundColor(Color.WHITE)
        }

        fun textView(text: String, sizeSp: Float, bold: Boolean, gravity: Int, color: Int = Color.BLACK): TextView {
            return TextView(context).apply {
                this.text = text
                setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)
                setTypeface(Typeface.DEFAULT, if (bold) Typeface.BOLD else Typeface.NORMAL)
                this.gravity = gravity
                setTextColor(color)
            }
        }

        fun separator() {
            root.addView(
                View(context).apply {
                    val lp = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dp(1)
                    )
                    lp.topMargin = dp(6)
                    lp.bottomMargin = dp(6)
                    layoutParams = lp
                    setBackgroundColor(Color.parseColor("#444444"))
                }
            )
        }

        root.addView(textView(shopName, 18f, true, Gravity.CENTER))
        if (showAddress && address.isNotBlank()) {
            root.addView(textView(address, 12f, false, Gravity.CENTER))
        }
        if (phone.isNotBlank()) {
            root.addView(textView("Phone: $phone", 12f, false, Gravity.CENTER))
        }

        separator()

        root.addView(textView("Bill No: $billNumber", 12f, true, Gravity.LEFT))
        root.addView(textView("Date: $date", 12f, false, Gravity.LEFT))
        root.addView(textView("Time: $time", 12f, false, Gravity.LEFT))

        separator()

        items.forEach { item ->
            root.addView(textView(item.name, 13f, true, Gravity.LEFT))
            root.addView(
                textView(
                    "${item.quantity} × ${Money.paiseToDisplay(item.ratePaise)} = ${Money.paiseToDisplay(item.amountPaise)}",
                    12f, false, Gravity.LEFT
                )
            )
        }

        separator()

        root.addView(
            textView(
                "Subtotal: ${Money.paiseToDisplay(subtotalPaise)}",
                13f, false, Gravity.LEFT
            )
        )
        if (discountPaise > 0) {
            root.addView(
                textView(
                    "Discount: ${Money.paiseToDisplay(discountPaise)}",
                    13f, false, Gravity.LEFT
                )
            )
        }
        root.addView(
            textView(
                "TOTAL: ${Money.paiseToDisplay(totalPaise)}",
                16f, true, Gravity.LEFT
            )
        )

        separator()

        root.addView(textView(thankYou, 12f, false, Gravity.CENTER))
        return root
    }
}

private class ReceiptPrintAdapter(
    private val context: Context,
    private val view: View
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
            val bitmap = render()
            FileOutputStream(destination.fileDescriptor).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: Exception) {
            callback.onWriteFailed(e.message)
        }
    }

    private fun render(): Bitmap {
        val density = context.resources.displayMetrics.density
        val widthPx = (384 * density).toInt().coerceAtLeast(384)
        view.measure(
            View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val heightPx = view.measuredHeight.coerceAtLeast(1)
        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}
