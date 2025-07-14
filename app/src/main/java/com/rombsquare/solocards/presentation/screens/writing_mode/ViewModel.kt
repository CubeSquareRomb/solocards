package com.rombsquare.solocards.presentation.screens.writing_mode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.prefs.PrefUseCases
import com.rombsquare.solocards.presentation.screens.writing_mode.models.UiEvent
import com.rombsquare.solocards.presentation.screens.writing_mode.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WritingGameVM @Inject constructor(
    private val cardUseCases: CardUseCases,
    prefUseCases: PrefUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val fileId = savedStateHandle.get<Int>("fileId")!!
    private val count = savedStateHandle.get<Int>("taskCount")!!

    private val showCorrectAnswer: Boolean = prefUseCases.getShowAnswer()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cards = cardUseCases.getCardsByFileId(fileId).shuffled().take(count)
            )
        }
    }

    fun next(userAnswer: String) {
        val currentCard = _uiState.value.cards[_uiState.value.lvl]
        if (userAnswer == currentCard.answer) {
            if (showCorrectAnswer) {
                showToast("Correct!")
            }
            _uiState.value = _uiState.value.copy(score=_uiState.value.score+1)
        } else {
            if (showCorrectAnswer) {
                showToast("Incorrect (${currentCard.answer})")
            }
        }

        _uiState.value = _uiState.value.copy(lvl=_uiState.value.lvl+1)
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiState.value.currentToast.emit(message)
        }
    }

    fun onEvent(event: UiEvent?) {
        when (event) {
            is UiEvent.ConfirmAnswer -> {
                next(event.userAnswer)

                if (_uiState.value.lvl >= _uiState.value.cards.size) {
                    _uiState.value = _uiState.value.copy(showEndGameDialog = true)
                }
            }

            null -> {}
        }
    }
}