package com.rombsquare.solocards.presentation.screens.editor

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rombsquare.solocards.presentation.screens.editor.components.dialogs.CreateCardDialog
import com.rombsquare.quiz.editor.DeleteDialog
import com.rombsquare.quiz.editor.PlaySettingsDialog
import com.rombsquare.solocards.presentation.screens.editor.components.dialogs.PlayDialog
import com.rombsquare.quiz.editor.RenameDialog
import com.rombsquare.solocards.presentation.screens.editor.components.EditorCardList
import com.rombsquare.solocards.presentation.screens.editor.components.PlayFab
import com.rombsquare.solocards.presentation.screens.editor.components.dialogs.EditCardDialog
import com.rombsquare.solocards.presentation.screens.editor.components.dialogs.TagDialog
import com.rombsquare.solocards.presentation.screens.editor.models.CurrentDialog

import com.rombsquare.solocards.presentation.screens.editor.models.UiAction
import com.rombsquare.solocards.presentation.screens.editor.models.UiEvent
import com.rombsquare.solocards.presentation.screens.editor.models.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    onEvent: (UiEvent?) -> Unit, // send command to view model
    uiState: State<UiState>, // values of this screen
    currentDialog: State<CurrentDialog?>,
    moveToMainMenu: () -> Unit,
    navigate: (String) -> Unit,
) {
    val context = LocalContext.current
    val uiState = uiState.value
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            uiState.currentToast.collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when (currentDialog.value) {
        CurrentDialog.CreateCard -> {
            CreateCardDialog(
                onConfirm = { q, ans -> onEvent(UiEvent.CreateCard(q, ans)) },
                onDismiss = { onEvent(null) },
            )
        }
        CurrentDialog.DeleteFile -> {
            DeleteDialog(
                onConfirm = {
                    onEvent(UiEvent.DeleteFile)
                    moveToMainMenu()
                },
                onDismiss = { onEvent(null) }
            )
        }
        CurrentDialog.EditCard -> {
            EditCardDialog(
                card = uiState.chosenCard!!,
                onConfirm = { onEvent(UiEvent.UpdateCard(it)) },
                onDelete = { onEvent(UiEvent.DeleteCard(uiState.chosenCard)) },
                onDismiss = { onEvent(null) }
            )
        }
        CurrentDialog.Play -> {
            PlayDialog(
                onPlay = { onEvent(UiEvent.ChooseMode(it)) },
                onDismiss = { onEvent(null) }
            )
        }

        CurrentDialog.PlaySettings -> {
            PlaySettingsDialog(
                onChooseAllCards = { onEvent(UiEvent.ChooseAllCards) },
                onConfirm = { onEvent(UiEvent.ConfirmTaskCount(it)) },
                onDismiss = { onEvent(null) }
            )
        }
        CurrentDialog.RenameFile -> {
            RenameDialog(
                onConfirm = { onEvent(UiEvent.RenameFile(it)) },
                onDismiss = { onEvent(null) }
            )
        }
        CurrentDialog.TagEditor -> {
            TagDialog(
                file = uiState.file,
                onConfirm = { onEvent(UiEvent.UpdateTags(it)) },
                onDismiss = { onEvent(null) }
            )
        }
        null -> {}
    }

    when (val action = uiState.action) {
        is UiAction.Route -> {
            navigate(action.route)
        }
        null -> {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                navigationIcon = {
                    IconButton(onClick = {
                        moveToMainMenu()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(UiEvent.ClickTagIcon) }) {
                        Icon(Icons.Default.Tag, contentDescription = "Tags")
                    }

                    IconButton(onClick = { onEvent(UiEvent.ClickDeleteIcon) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }

                    IconButton(onClick = { onEvent(UiEvent.ClickEditIcon) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }

                    IconButton(onClick = { onEvent(UiEvent.ClickFavoriteIcon) }) {
                        Icon(if (uiState.file.isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                }
            )
        },
        floatingActionButton = {
            PlayFab { onEvent(UiEvent.ClickPlayFab) }
        },
        content = { innerPadding ->
            EditorCardList(
                modifier = Modifier.padding(innerPadding),
                cards = uiState.cards,
                onCardClick = { onEvent(UiEvent.SelectCard(it)) },
                onAddClick = { onEvent(UiEvent.ClickAddCard) }
            )
        }
    )
}
