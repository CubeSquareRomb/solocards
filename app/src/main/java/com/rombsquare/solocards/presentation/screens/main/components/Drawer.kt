package com.rombsquare.solocards.presentation.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Drawer(
    onChooseTag: (String) -> Unit,
    tags: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .width(260.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            listOf(
                "all" to "All",
                "fav" to "Favorite",
                "trash" to "Trash"
            ).forEach { (tag, label) ->
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    onClick = { onChooseTag(tag) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RectangleShape,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically 
                    ) {
                        val icon = when (tag) {
                            "trash" -> Icons.Default.Delete
                            "fav" -> Icons.Default.Favorite
                            else -> Icons.Default.Menu
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = label
                        )

                        Spacer(Modifier.size(20.dp))

                        Text(
                            label,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(1f)
                        )
                    }

                }
            }

            HorizontalDivider(Modifier, 1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha=0.5f))
        }

        items(tags) {
            if (it.isEmpty()) {
                return@items
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                onClick = { onChooseTag(it) },
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = it
                    )

                    Spacer(Modifier.size(20.dp))

                    Text(
                        it,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}