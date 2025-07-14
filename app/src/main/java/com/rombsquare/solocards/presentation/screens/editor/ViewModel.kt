package com.rombsquare.solocards.presentation.screens.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.files.FileUseCases
import com.rombsquare.solocards.domain.usecases.validators.ValidatorUseCases
import com.rombsquare.solocards.presentation.screens.editor.models.CurrentDialog
import com.rombsquare.solocards.presentation.screens.editor.models.UiAction
import com.rombsquare.solocards.presentation.screens.editor.models.UiEvent
import com.rombsquare.solocards.presentation.screens.editor.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorVM @Inject constructor(
    private val cardUseCases: CardUseCases,
    private val fileUseCases: FileUseCases,
    private val validatorUseCases: ValidatorUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _currentDialog: MutableStateFlow<CurrentDialog?> = MutableStateFlow(null)
    val currentDialog: StateFlow<CurrentDialog?> = _currentDialog

    private val fileId = savedStateHandle.get<Int>("fileId")!!

    init {
        viewModelScope.launch {
            val cards = cardUseCases.getCardsByFileId(fileId)
            val file = fileUseCases.getFileById(fileId)!!
            _uiState.value = _uiState.value.copy(
                cards = cards,
                file = file
            )
        }
    }

    fun setUiAction(uiAction: UiAction?) {
        _uiState.value = _uiState.value.copy(action = uiAction)
    }

    fun setCurrentDialog(dialog: CurrentDialog?) {
        _currentDialog.value = dialog
    }

    suspend fun refreshCards() {
        _uiState.value = _uiState.value.copy(
            cards = cardUseCases.getCardsByFileId(fileId)
        )
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiState.value.currentToast.emit(message)
        }
    }

    fun onEvent(event: UiEvent?) {
        when (event) {
            is UiEvent.SelectCard -> {
                _uiState.value = _uiState.value.copy(
                    chosenCard = event.card
                )

                setCurrentDialog(CurrentDialog.EditCard)
            }

            UiEvent.ClickAddCard -> setCurrentDialog(CurrentDialog.CreateCard)

            is UiEvent.CreateCard -> {
                val card = Card(event.question, event.answer, fileId)

                val result = validatorUseCases.validateCard(card, _uiState.value.cards, false)
                if (!result.isSuccess) {
                    showToast(result.errorMessage!!)
                    return
                }

                viewModelScope.launch {
                    cardUseCases.addCard(card)
                    refreshCards()
                    onEvent(null)
                }
            }

            is UiEvent.UpdateCard -> {
                val result = validatorUseCases.validateCard(event.card, _uiState.value.cards, true)
                if (!result.isSuccess) {
                    showToast(result.errorMessage!!)
                    return
                }

                viewModelScope.launch {
                    cardUseCases.updateCard(event.card)
                    refreshCards()
                    onEvent(null)
                }
            }

            is UiEvent.DeleteCard -> {
                viewModelScope.launch {
                    cardUseCases.deleteCard(event.card)
                    showToast("Card was deleted")
                    refreshCards()
                    onEvent(null)
                }
            }

            is UiEvent.UpdateTags -> {
                val result = validatorUseCases.validateTags(event.tags)
                if (!result.isSuccessful) {
                    showToast(result.errorMessage!!)
                    return
                }

                viewModelScope.launch {
                    val file = _uiState.value.file.copy(tags = event.tags)
                    _uiState.value = _uiState.value.copy(file = file)
                    fileUseCases.updateFile(file)
                    onEvent(null)
                }
            }

            is UiEvent.RenameFile -> {
                val result = validatorUseCases.validateName(event.newName)
                if (!result.isSuccessful) {
                    showToast(result.errorMessage!!)
                    return
                }

                viewModelScope.launch {
                    fileUseCases.updateFile(
                        _uiState.value.file.copy(name = event.newName)
                    )
                    onEvent(null)
                }
            }

            is UiEvent.ChooseMode -> {
                _uiState.value = _uiState.value.copy(chosenMode = event.mode)
                setCurrentDialog(CurrentDialog.PlaySettings)
            }

            UiEvent.DeleteFile -> {
                viewModelScope.launch {
                    fileUseCases.trashFile(_uiState.value.file.id!!)
                }
            }

            is UiEvent.ConfirmTaskCount -> {
                val count = event.count.coerceIn(4, _uiState.value.cards.size)
                setUiAction(UiAction.Route("${_uiState.value.chosenMode!!.route}/${_uiState.value.file.id}/$count"))
            }

            UiEvent.ClickEditIcon -> setCurrentDialog(CurrentDialog.RenameFile)

            UiEvent.ClickDeleteIcon -> setCurrentDialog(CurrentDialog.DeleteFile)

            UiEvent.ClickFavoriteIcon -> {
                val file = _uiState.value.file.copy(isFav = !_uiState.value.file.isFav)
                viewModelScope.launch {
                    fileUseCases.updateFile(file)
                    _uiState.value = _uiState.value.copy(file = file)
                }
            }

            UiEvent.ClickTagIcon -> { setCurrentDialog(CurrentDialog.TagEditor) }

            UiEvent.ClickPlayFab -> {
                if (_uiState.value.cards.size < 4) {
                    showToast("You need at least 4 cards")
                    return
                }

                setCurrentDialog(CurrentDialog.Play)
            }

            UiEvent.ChooseAllCards -> {
                val count = _uiState.value.cards.size
                setUiAction(UiAction.Route("${_uiState.value.chosenMode!!.route}/${_uiState.value.file.id}/$count"))
            }

            null -> {
                setUiAction(null)
                setCurrentDialog(null)
            }
        }
    }
}