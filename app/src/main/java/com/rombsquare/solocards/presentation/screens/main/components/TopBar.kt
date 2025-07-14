package com.rombsquare.solocards.presentation.screens.main.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rombsquare.solocards.domain.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    chosenTag: String,
    onMenuClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    user: User?
) {
    TopAppBar(
        title = { Text("Quizzes: ${chosenTag.replaceFirstChar { it.uppercaseChar() }}") },
        navigationIcon = {
            IconButton(
                onClick = onMenuClicked
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 12.dp, end = 8.dp)
                )
            }

        },
        actions = {
            IconButton(onClick = onSettingsClicked) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }

            IconButton(onClick = onProfileClicked) {
                if (user == null) {
                    Icon(Icons.Default.Person, contentDescription = "Profile")
                    return@IconButton
                }

                AsyncImage(
                    model = user.profilePictureUrl,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }
    )
}