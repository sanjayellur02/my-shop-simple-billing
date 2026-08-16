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
