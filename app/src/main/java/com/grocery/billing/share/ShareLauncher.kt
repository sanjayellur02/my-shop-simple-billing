package com.grocery.billing.share

import android.content.ComponentName
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
     * Opens WhatsApp with the bill attached as a PDF and the recipient's chat
     * pre-selected using WhatsApp's "jid" extra. Falls back to the contact
     * picker if WhatsApp can't resolve the direct target.
     */
    fun openWhatsAppPdfToNumber(
        context: Context,
        pdfUri: Uri,
        phoneNumber: String,
        caption: String = ""
    ) {
        val jid = waJid(phoneNumber)
        if (jid != null) {
            val direct = Intent(Intent.ACTION_SEND).apply {
                component = ComponentName("com.whatsapp", "com.whatsapp.ContactPicker")
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, pdfUri)
                putExtra("jid", jid)
                if (caption.isNotBlank()) putExtra(Intent.EXTRA_TEXT, caption)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (direct.resolveActivity(context.packageManager) != null) {
                context.startActivity(direct)
                return
            }
        }
        openWhatsAppPdf(context, pdfUri, caption)
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

    /** Converts a phone number to a WhatsApp JID (assumes Indian numbers). */
    private fun waJid(phoneNumber: String): String? {
        var digits = phoneNumber.filter { it.isDigit() }
        if (digits.isEmpty()) return null
        if (digits.startsWith("0")) digits = digits.drop(1)
        if (digits.length == 10) digits = "91$digits"
        return if (digits.length in 11..15) "${digits}@s.whatsapp.net" else null
    }
}
