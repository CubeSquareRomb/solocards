package com.rombsquare.solocards.presentation.screens.training_mode.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ShowAnswerDialog(
    answer: String,
    onDismiss: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = {onDismiss(false)},
        title = {
            Text("Answer: $answer", fontSize = 20.sp)
        },
        text = { Text("Did you think about this answer?") },
        confirmButton = {
            Row {
                TextButton(onClick = {onDismiss(true)}) { Text("Yes") }
                TextButton(onClick = {onDismiss(false)}) { Text("No") }
            }
        },
        dismissButton = {}
    )
}