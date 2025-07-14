package com.rombsquare.solocards.presentation.screens.main

import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.usecases.ai.AiUseCases
import com.rombsquare.solocards.domain.usecases.files.FileUseCases
import com.rombsquare.solocards.domain.usecases.firestore.FirestoreUseCases
import com.rombsquare.solocards.domain.usecases.prefs.PrefUseCases
import com.rombsquare.solocards.presentation.screens.main.models.Dialog
import com.rombsquare.solocards.presentation.screens.main.models.UiAction
import com.rombsquare.solocards.presentation.screens.main.models.UiEvent
import com.rombsquare.solocards.presentation.screens.main.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuVM @Inject constructor(
    private val fileUseCases: FileUseCases,
    private val prefUseCases: PrefUseCases,
    private val firestoreUseCases: FirestoreUseCases,
    private val aiUseCases: AiUseCases,
    private val googleAuthUiClient: GoogleAuthUiClient,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            refreshFiles()
            _uiState.value = _uiState.value.copy(
                tags = fileUseCases.getTags(),
                showAnswerParam = prefUseCases.getShowAnswer(),
            )

            loadCurrentUser()
            if (_uiState.value.currentUser != null) {
                firestoreUseCases.refillTokensIfPossible(_uiState.value.currentUser!!.userId)
            }
        }
    }

    private fun loadCurrentUser() {
        val user = googleAuthUiClient.getSignedInUser()
        if (user != null) {
            _uiState.value = _uiState.value.copy(currentUser = user)
        }
    }

    suspend fun refreshFiles() {
        _uiState.value = _uiState.value.copy(
            files = fileUseCases.getFilesByTag(_uiState.value.selectedTag)
        )
    }

    fun setUiAction(uiAction: UiAction?) {
        _uiState.value = _uiState.value.copy(action = uiAction)
    }

    fun setDialog(dialog: Dialog?) {
        _uiState.value = _uiState.value.copy(dialog = dialog)
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiState.value.currentToast.emit(message)
        }
    }

    fun onLastDatabaseActionCalled() {
        _uiState.value = _uiState.value.copy(
            lastDatabaseActionTime = System.currentTimeMillis()
        )
    }

    fun onEvent(event: UiEvent?) {
        when (event) {
            UiEvent.LoadFiles -> viewModelScope.launch {
                val files = fileUseCases.getFiles()
                _uiState.value = _uiState.value.copy(files = files)
            }

            is UiEvent.SelectFile -> {
                _uiState.value = _uiState.value.copy(chosenFile = event.file)
                if (_uiState.value.selectedTag == "trash") {
                    setDialog(Dialog.RestoreFile)
                } else {
                    setUiAction(UiAction.OpenFile)
                }
            }

            is UiEvent.ChooseTag -> {
                viewModelScope.launch {
                    val files = fileUseCases.getFilesByTag(event.tag)
                    _uiState.value = _uiState.value.copy(
                        files = files,
                        selectedTag = event.tag,
                        drawerValue = DrawerValue.Closed
                    )
                }
            }

            is UiEvent.CreateFile -> {
                viewModelScope.launch {
                    fileUseCases.addFile(
                        File(event.name),
                        _uiState.value.selectedTag
                    )

                    onEvent(null)
                    refreshFiles()
                }
            }

            is UiEvent.SignIn -> {
                _uiState.value = _uiState.value.copy(currentUser = event.user)
            }

            is UiEvent.SetShowAnswer -> {
                prefUseCases.setShowAnswer(event.state)
                _uiState.value = _uiState.value.copy(showAnswerParam = event.state)
            }

            is UiEvent.GenerateWithAi -> {
                val uid = _uiState.value.currentUser!!.userId
                showToast("Wait")
                viewModelScope.launch {
                    if (!firestoreUseCases.areEnoughTokens(uid, 25)) {
                        showToast("No tokens. Wait until tomorrow")
                        return@launch
                    }

                    val state = aiUseCases.generateCards(event.name, event.descr)
                    if (!state) {
                        showToast("Something went wrong")
                        return@launch
                    }

                    firestoreUseCases.subtractAiTokens(uid, 25)
                    onEvent(null)
                    showToast("Generated")
                    refreshFiles()
                }
            }

            UiEvent.ShowCreateFileDialog -> {
                setDialog(Dialog.CreateFile)
            }

            UiEvent.OpenDrawer -> {
                _uiState.value = _uiState.value.copy(drawerValue = DrawerValue.Open)
            }

            UiEvent.CloseDrawer -> {
                _uiState.value = _uiState.value.copy(drawerValue = DrawerValue.Closed)
            }

            UiEvent.ClearTrash -> {
                viewModelScope.launch {
                    fileUseCases.clearTrash()
                    onEvent(null)
                    refreshFiles()
                }
            }

            UiEvent.RestoreFile -> {
                viewModelScope.launch {
                    fileUseCases.restoreFile(_uiState.value.chosenFile?.id!!)
                    onEvent(null)
                    refreshFiles()
                }
            }

            UiEvent.OpenSettings -> {
                setDialog(Dialog.Settings)
            }

            UiEvent.ShowClearTrashDialog -> {
                setDialog(Dialog.ClearTrash)
            }

            UiEvent.OpenProfileDialog -> {
                setDialog(Dialog.Profile)
            }

            UiEvent.SignOut -> {
                _uiState.value = _uiState.value.copy(currentUser = null)
            }

            UiEvent.ShowLicenceDialog -> {
                setDialog(Dialog.Licence)
            }

            UiEvent.SaveData -> {
                viewModelScope.launch {
                    if (System.currentTimeMillis() - _uiState.value.lastDatabaseActionTime < 10_000) {
                        showToast("Access denied. Wait few seconds")
                        return@launch
                    }

                    onLastDatabaseActionCalled()
                    showToast("Saving data...")
                    val state = firestoreUseCases.saveData(_uiState.value.currentUser!!.userId)
                    showToast(if (state) "Saved successful" else "Something went wrong")
                    onLastDatabaseActionCalled()
                }
            }

            UiEvent.LoadData -> {
                viewModelScope.launch {
                    if (System.currentTimeMillis() - _uiState.value.lastDatabaseActionTime < 10_000) {
                        showToast("Access denied. Wait few seconds")
                        return@launch
                    }

                    onLastDatabaseActionCalled()
                    showToast("Loading data...")
                    val state = firestoreUseCases.loadData(_uiState.value.currentUser!!.userId)
                    refreshFiles()
                    showToast(if (state) "Loaded successful" else "Something went wrong")
                    onLastDatabaseActionCalled()
                }
            }

            UiEvent.ClickAiMode -> {
                if (_uiState.value.currentUser == null) {
                    showToast("You need to sign in")
                    return
                }
                setDialog(Dialog.Ai)
            }

            null -> {
                setDialog(null)
                setUiAction(null)
            }
        }
    }
}