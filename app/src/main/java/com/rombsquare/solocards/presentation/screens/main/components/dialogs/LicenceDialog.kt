package com.rombsquare.solocards.presentation.screens.main.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

val licenseText = """
    By clicking "Agree" button below, you agree that:
    
    1. Your profile icon is shown only in the top app bar as a clickable profile button.
    2. Your username is displayed solely as the title in your Profile dialog.
    3. Your user ID is used exclusively to fetch and manage your personal data from Firestore.
    
    We respect your privacy and do not use your data for any other purposes.
    
    If you don’t agree, please click the "Not agree" button, and don't sign in
""".trimIndent()

@Composable
fun LicenceDialog(
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("License") },
        text = { Text(licenseText) },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text("Agree")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not agree")
            }
        }
    )
}