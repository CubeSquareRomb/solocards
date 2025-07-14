package com.rombsquare.solocards.presentation.screens.editor.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.rombsquare.solocards.domain.models.File

@Composable
fun TagDialog(
    file: File,
    onConfirm: (List<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf(
        "#" + file.tags.joinToString(" #")
    )}

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tags") },
        text = {
            Column {
                Text("Edit your tags to this quiz. Example:")
                Text("#biology #hard #flora", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Tags") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (text.isEmpty()) {
                    return@TextButton
                }

                val list = text
                    .substring(1)
                    .replace(" ", "")
                    .split("#")

                onConfirm(list)

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