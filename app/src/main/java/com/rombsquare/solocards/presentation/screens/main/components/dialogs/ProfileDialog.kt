package com.rombsquare.solocards.presentation.screens.main.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.rombsquare.solocards.domain.models.User

@Composable
fun ProfileDialog(
    currentUser: User?,
    onSignOut: () -> Unit,
    onSignIn: () -> Unit,
    onSaveData: () -> Unit,
    onLoadData: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(currentUser?.username ?: "Profile") },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (currentUser == null) {
                        Text("Sign in to save your quizzes to the cloud")
                    } else {
                        Button(onClick = onSaveData) {
                            Text("Save data to cloud")
                        }

                        Button(onClick = onLoadData) {
                            Text("Load data from cloud")
                        }

                        Text("Current data will be deleted when loading data from the cloud", textAlign = TextAlign.Center)
                    }
                }
            }
        },

        confirmButton = {
            if (currentUser == null) {
                TextButton(onClick = onSignIn) {
                    Text("Sign In")
                }
            } else {
                TextButton(onClick = onSignOut) {
                    Text("Sign Out")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Back")
            }
        }
    )
}