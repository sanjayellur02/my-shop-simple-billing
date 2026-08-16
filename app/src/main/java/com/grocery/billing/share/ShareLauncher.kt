package com.grocery.billing.share

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Launches WhatsApp / SMS / system share. Never sends anything silently -
 * the user always presses Send in the target app.
 */
object ShareLauncher {

    /** Tries the WhatsApp app with the number and message; falls back to wa.me. */
    fun openWhatsApp(context: Context, phoneNumber: String, text: String) {
        val digits = phoneNumber.filter { it.isDigit() }
        val sendToWa = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$digits")).apply {
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (sendToWa.resolveActivity(context.packageManager) != null) {
            context.startActivity(sendToWa)
            return
        }
        val waMe = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://wa.me/$digits?text=" + Uri.encode(text))
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(waMe)
    }

    fun openSms(context: Context, phoneNumber: String, text: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${phoneNumber.trim()}")).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra("sms_body", text)
        }
        context.startActivity(intent)
    }

    /**
     * Opens WhatsApp with the bill attached as a PDF. WhatsApp shows its own
     * contact picker (it does not accept a prefilled number for attachments);
     * if WhatsApp is not installed the PDF is offered through the share sheet.
     */
    fun openWhatsAppPdf(context: Context, pdfUri: Uri, caption: String = "") {
        val whatsapp = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_STREAM, pdfUri)
            if (caption.isNotBlank()) putExtra(Intent.EXTRA_TEXT, caption)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        if (whatsapp.resolveActivity(context.packageManager) != null) {
            context.startActivity(whatsapp)
            return
        }
        val generic = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, pdfUri)
            if (caption.isNotBlank()) putExtra(Intent.EXTRA_TEXT, caption)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(generic, "Share Bill (PDF)"))
    }

    fun openShareSheet(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Share Bill"))
    }

    fun isValidPhoneNumber(input: String): Boolean {
        val digits = input.filter { it.isDigit() }
        return digits.length in 7..15
    }
}
