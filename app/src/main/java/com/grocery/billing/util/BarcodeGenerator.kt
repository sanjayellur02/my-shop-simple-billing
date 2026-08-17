package com.grocery.billing.util

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object BarcodeGenerator {

    private val writer = MultiFormatWriter()

    fun generate(text: String, width: Int = 600, height: Int = 200): Bitmap? {
        if (text.isBlank()) return null
        return try {
            val hints = mapOf(
                EncodeHintType.MARGIN to 0
            )
            val matrix: BitMatrix = writer.encode(text, BarcodeFormat.CODE_39, width, height, hints)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (matrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                }
            }
            bitmap
        } catch (_: Exception) {
            null
        }
    }
}
