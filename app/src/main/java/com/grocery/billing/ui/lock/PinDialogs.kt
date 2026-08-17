package com.grocery.billing.ui.lock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.grocery.billing.ui.components.ErrorText

@Composable
fun PinField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onDone: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.filter(Char::isDigit).take(4)) },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier,
        isError = isError,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = if (onDone != null) ImeAction.Done else ImeAction.Default
        ),
        keyboardActions = if (onDone != null) KeyboardActions(onDone = { onDone() }) else KeyboardActions(),
        textStyle = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun PinSetupDialog(
    onDismiss: () -> Unit,
    onSetPin: (String) -> Unit
) {
    var first by remember { mutableStateOf("") }
    var second by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set App Lock PIN") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Choose a 4-digit PIN. You'll need it to open the app.",
                    style = MaterialTheme.typography.bodyMedium
                )
                PinField(
                    value = first,
                    onValueChange = { first = it; error = null },
                    label = "New PIN",
                    isError = error != null
                )
                PinField(
                    value = second,
                    onValueChange = { second = it; error = null },
                    label = "Confirm PIN"
                )
                error?.let { ErrorText(it) }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    first.length != 4 -> error = "PIN must be exactly 4 digits."
                    second != first -> error = "PINs do not match."
                    else -> onSetPin(first)
                }
            }) {
                Text("Set PIN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun PinVerifyDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onVerified: () -> Unit,
    verify: (String) -> Boolean
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(message, style = MaterialTheme.typography.bodyMedium)
                PinField(
                    value = pin,
                    onValueChange = { pin = it; error = null },
                    label = "Current PIN",
                    isError = error != null
                )
                error?.let { ErrorText(it) }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (verify(pin)) onVerified()
                else error = "Wrong PIN. Try again."
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
