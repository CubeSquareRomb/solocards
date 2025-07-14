package com.rombsquare.solocards.presentation.screens.main

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rombsquare.quiz.quizlist.AddDeleteFab
import com.rombsquare.quiz.quizlist.ClearTrashDialog
import com.rombsquare.quiz.quizlist.RestoreDialog
import com.rombsquare.solocards.presentation.screens.main.components.Drawer
import com.rombsquare.solocards.presentation.screens.main.components.FileList
import com.rombsquare.solocards.presentation.screens.main.components.TopBar
import com.rombsquare.solocards.presentation.screens.main.components.dialogs.CreateFileDialog
import com.rombsquare.solocards.presentation.screens.main.components.dialogs.ProfileDialog
import com.rombsquare.solocards.presentation.screens.main.components.dialogs.SettingsDialog
import com.rombsquare.solocards.presentation.screens.main.models.Dialog
import com.rombsquare.solocards.presentation.screens.main.models.UiAction
import com.rombsquare.solocards.presentation.screens.main.models.UiEvent
import com.rombsquare.solocards.presentation.screens.main.models.UiState
import com.rombsquare.solocards.domain.models.User
import com.rombsquare.solocards.presentation.screens.main.components.dialogs.GenerateCards
import com.rombsquare.solocards.presentation.screens.main.components.dialogs.LicenceDialog
import kotlinx.coroutines.launch

@Composable
fun MainMenuScreen(
    onEvent: (UiEvent?) -> Unit, // send command to view model
    uiState: State<UiState>, // values of this screen
    moveToEditor: (Int) -> Unit,
    onSignIn: () -> Unit,
    user: User?,
    onSignOut: () -> Unit
) {
    val uiState = uiState.value
    val drawerState = rememberSyncedDrawerState(uiState.drawerValue, onEvent)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            uiState.currentToast.collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (user != null) {
            onEvent(UiEvent.SignIn(user))
        }
    }

    LaunchedEffect(uiState.action) {
        when (uiState.action) {
            UiAction.OpenFile -> {
                onEvent(null)
                moveToEditor(uiState.chosenFile?.id!!)
            }

            null -> {}
        }
    }

    when (uiState.dialog) {
        Dialog.CreateFile -> CreateFileDialog(
            onAccept = { onEvent(UiEvent.CreateFile(it)) },
            onAiModeClicked = { onEvent(UiEvent.ClickAiMode) },
            onDismiss = { onEvent(null) }
        )

        Dialog.ClearTrash -> ClearTrashDialog(
            onConfirm = { onEvent(UiEvent.ClearTrash ) },
            onDismiss = { onEvent(null) }
        )

        Dialog.RestoreFile -> RestoreDialog(
            onConfirm = { onEvent(UiEvent.RestoreFile) },
            onDismiss = { onEvent(null) }
        )

        Dialog.Settings -> SettingsDialog(
            showAnswerParam = uiState.showAnswerParam,
            onShowAnswerChecked = { onEvent(UiEvent.SetShowAnswer(it)) },
            onDismiss = { onEvent(null) }
        )

        Dialog.Profile -> ProfileDialog(
            currentUser = uiState.currentUser,
            onSignIn = { onEvent(UiEvent.ShowLicenceDialog) },
            onSignOut = onSignOut,
            onSaveData = { onEvent(UiEvent.SaveData) },
            onLoadData = { onEvent(UiEvent.LoadData) },
            onDismiss = { onEvent(null) }
        )

        Dialog.Licence -> LicenceDialog(
            onAccept = { onSignIn(); onEvent(null) } ,
            onDismiss = { onEvent(null) }
        )

        Dialog.Ai -> GenerateCards(
            onConfirm = { name, descr -> onEvent(UiEvent.GenerateWithAi(name, descr)) },
            onDismiss = { onEvent(null) }
        )
        
        null -> {}
    }

    ModalNavigationDrawer(
        drawerContent = {
            Drawer(
                onChooseTag = { onEvent(UiEvent.ChooseTag(it)) },
                tags = uiState.tags
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    chosenTag = if (uiState.selectedTag == "fav") "favorite" else uiState.selectedTag,
                    onMenuClicked = { onEvent(UiEvent.OpenDrawer) },
                    onSettingsClicked = { onEvent(UiEvent.OpenSettings) },
                    onProfileClicked = { onEvent(UiEvent.OpenProfileDialog) },
                    user = uiState.currentUser
                )
            },
            floatingActionButton = {
                AddDeleteFab(
                    chosenTag = uiState.selectedTag,
                    onCreate = { onEvent(UiEvent.ShowCreateFileDialog) },
                    onDelete = { onEvent(UiEvent.ShowClearTrashDialog) }
                )
            },
            content = { innerPadding ->
                FileList(
                    modifier = Modifier.padding(innerPadding),
                    files = uiState.files,
                    onFileClick = { onEvent(UiEvent.SelectFile(it)) }
                )
            }
        )
    }
}

@Composable
fun rememberSyncedDrawerState(targetValue: DrawerValue, onEvent: (UiEvent) -> Unit): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    LaunchedEffect(targetValue) {
        if (targetValue == DrawerValue.Open) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    LaunchedEffect(drawerState.currentValue) {
        if (drawerState.currentValue == DrawerValue.Open) {
            onEvent(UiEvent.OpenDrawer)
        } else if (drawerState.currentValue == DrawerValue.Closed) {
            onEvent(UiEvent.CloseDrawer)
        }
    }

    return drawerState
}