package com.rombsquare.solocards.presentation.screens.editor.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rombsquare.solocards.domain.models.Card

@Composable
fun EditCardDialog(
    card: Card,
    onConfirm: (Card) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var question by remember { mutableStateOf(card.question) }
    var answer by remember { mutableStateOf(card.answer) }
    var option1 by remember { mutableStateOf(card.option1) }
    var option2 by remember { mutableStateOf(card.option2) }
    var option3 by remember { mutableStateOf(card.option3) }
    var fixedOptions by remember { mutableStateOf(card.fixedOptions) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Card") },
        text = {
            Column {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Change question") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Change answer") },
                    singleLine = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = fixedOptions,
                        onCheckedChange = { fixedOptions = it }
                    )

                    Text("Fixed Options")
                }


                if (!fixedOptions) {
                    return@Column
                }

                OutlinedTextField(
                    value = option1,
                    onValueChange = { option1 = it },
                    label = { Text("Incorrect option #1") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = option2,
                    onValueChange = { option2 = it },
                    label = { Text("Incorrect option #2") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = option3,
                    onValueChange = { option3 = it },
                    label = { Text("Incorrect option #3") },
                    singleLine = true
                )

            }
        },

        confirmButton = {
            TextButton(onClick = { onConfirm(Card(question, answer, card.fileId, fixedOptions, option1, option2, option3, card.id)) }) {
                Text("OK")
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = onDelete) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Discard")
                }
            }
        }
    )
}