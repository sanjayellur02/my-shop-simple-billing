package com.grocery.billing.ui.billing

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.grocery.billing.share.BillPdf
import com.grocery.billing.share.ShareBillData
import com.grocery.billing.share.ShareLauncher
import com.grocery.billing.ui.components.ErrorText

/**
 * Share dialog: enter / select a mobile number, then send the bill via
 * WhatsApp or SMS. WhatsApp sends the bill as a PDF; SMS sends plain text.
 * If WhatsApp is unavailable, the bill is automatically sent via SMS instead.
 * If both are unavailable, a clear error message is shown.
 *
 * @param onBeforeShare Optional callback invoked before any external app is
 *   launched (e.g. to force-save the current draft).
 */
@Composable
fun ShareBillDialog(
    data: ShareBillData,
    onDismiss: () -> Unit,
    onBeforeShare: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var number by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val contactPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            number = queryContactNumber(context, uri)
            error = null
        }
    }

    fun shareWithNumber(launch: (String) -> Unit) {
        if (!ShareLauncher.isValidPhoneNumber(number)) {
            error = "Please enter a valid mobile number."
            return
        }
        onBeforeShare?.invoke()
        launch(number)
        onDismiss()
    }

    /**
     * WhatsApp with PDF: try WhatsApp → fallback to SMS → show error if both fail.
     * Does NOT dismiss the dialog on failure so the user keeps their data.
     */
    fun shareWhatsAppPdf() {
        if (!ShareLauncher.isValidPhoneNumber(number)) {
            error = "Please enter a valid mobile number."
            return
        }
        onBeforeShare?.invoke()
        val uri = BillPdf.generateShareUri(context, data)

        // 1. Try WhatsApp with PDF attachment
        if (uri != null) {
            val launched = ShareLauncher.openWhatsAppPdfToNumber(
                context, uri, number, caption = "Bill ${data.billNumber}"
            )
            if (launched) {
                onDismiss()
                return
            }
        }

        // 2. Try WhatsApp with plain text
        if (ShareLauncher.openWhatsApp(context, number, data.text)) {
            onDismiss()
            return
        }

        // 3. Try SMS as fallback
        if (ShareLauncher.openSms(context, number, data.text)) {
            onDismiss()
            return
        }

        // 4. Nothing available
        error = "WhatsApp and SMS are not available on this device."
    }

    /**
     * SMS only: try SMS → show error if unavailable.
     * Does NOT dismiss the dialog on failure.
     */
    fun shareSms() {
        if (!ShareLauncher.isValidPhoneNumber(number)) {
            error = "Please enter a valid mobile number."
            return
        }
        onBeforeShare?.invoke()
        if (ShareLauncher.openSms(context, number, data.text)) {
            onDismiss()
            return
        }
        error = "SMS is not available on this device."
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Send Bill", style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Mobile Number", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = number,
                    onValueChange = {
                        number = it
                        error = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                OutlinedButton(
                    onClick = {
                        contactPicker.launch(
                            Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Contact")
                }
                error?.let { ErrorText(it) }

                HorizontalDivider()

                ShareActionButton("WhatsApp (PDF)") {
                    shareWhatsAppPdf()
                }
                ShareActionButton("Send via SMS") {
                    shareSms()
                }
                ShareActionButton("Other Share Options") {
                    onBeforeShare?.invoke()
                    ShareLauncher.openShareSheet(context, data.text)
                    onDismiss()
                }
                Text(
                    "WhatsApp opens that customer's chat with the bill PDF ready to send.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun ShareActionButton(text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(52.dp)
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

private fun queryContactNumber(context: Context, uri: Uri): String {
    return try {
        context.contentResolver.query(
            uri,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) ?: "" else ""
        } ?: ""
    } catch (e: Exception) {
        ""
    }
}
