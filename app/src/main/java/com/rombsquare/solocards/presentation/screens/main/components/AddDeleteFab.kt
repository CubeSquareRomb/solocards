package com.rombsquare.quiz.quizlist

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AddDeleteFab(
    chosenTag: String,
    onCreate: () -> Unit,
    onDelete: () -> Unit
) {
    FloatingActionButton(
        onClick = { if (chosenTag == "trash") onDelete() else onCreate() },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = if (chosenTag == "trash") Icons.Filled.Delete else Icons.Filled.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}