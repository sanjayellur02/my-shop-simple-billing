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
 * Share dialog: enter / select a mobile number, then choose WhatsApp, SMS
 * or the Android share sheet. WhatsApp sends the bill as a PDF (recipient is
 * picked inside WhatsApp); SMS sends plain text. Nothing is sent without the
 * user's action.
 */
@Composable
fun ShareBillDialog(
    data: ShareBillData,
    onDismiss: () -> Unit
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
        launch(number)
        onDismiss()
    }

    fun shareWhatsAppPdf() {
        if (!ShareLauncher.isValidPhoneNumber(number)) {
            error = "Please enter a valid mobile number."
            return
        }
        val uri = BillPdf.generateShareUri(context, data)
        if (uri != null) {
            ShareLauncher.openWhatsAppPdfToNumber(context, uri, number, caption = "Bill ${data.billNumber}")
            onDismiss()
        } else {
            ShareLauncher.openWhatsApp(context, number, data.text)
            onDismiss()
        }
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
                    shareWithNumber { n -> ShareLauncher.openSms(context, n, data.text) }
                }
                ShareActionButton("Other Share Options") {
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
