package com.rombsquare.solocards.presentation.screens.editor.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.rombsquare.solocards.presentation.screens.editor.models.Mode

@Composable
fun PlayDialog(
    onPlay: (Mode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Choose mode")},
        text = {
            Column {
                TextButton(onClick = {onPlay(Mode.TRAINING)}) { Text("Training mode") }
                TextButton(onClick = {onPlay(Mode.OPTIONS)}) { Text("Option mode") }
                TextButton(onClick = {onPlay(Mode.WRITING)}) { Text("Writing mode") }
                TextButton(onClick = {onPlay(Mode.YES_NO)}) { Text("True/False mode") }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}