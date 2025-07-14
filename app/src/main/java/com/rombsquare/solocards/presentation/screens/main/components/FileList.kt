package com.rombsquare.solocards.presentation.screens.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rombsquare.solocards.domain.models.File
import kotlin.collections.lastIndex

@Composable
fun FileList(
    modifier: Modifier = Modifier,
    files: List<File>,
    onFileClick: (File) -> Unit
) {
    if (files.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No quizzes yet",
                fontSize = 20.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .then(modifier)
    ) {
        itemsIndexed(files) { index, file ->
            FileItem(file) {
                onFileClick(file)
            }

            if (index != files.lastIndex) {
                HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha=0.5f))
            }
        }

    }
}