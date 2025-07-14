package com.rombsquare.solocards.presentation.screens.option_mode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.game.GameUseCases
import com.rombsquare.solocards.domain.usecases.prefs.PrefUseCases
import com.rombsquare.solocards.presentation.screens.option_mode.models.UiEvent
import com.rombsquare.solocards.presentation.screens.option_mode.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionGameVM @Inject constructor(
    private val cardUseCases: CardUseCases,
    private val gameUseCases: GameUseCases,
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
            _uiState.value = _uiState.value.copy(cards = cardUseCases.getCardsByFileId(fileId).shuffled().take(count))
            randomizeOptions()
        }
    }

    fun randomizeOptions() {
        val card = _uiState.value.card!!

        var options: List<String>
        var correctIndex: Int

        if (card.fixedOptions) {
            options = listOf(card.answer, card.option1, card.option2, card.option3).shuffled()
            correctIndex = options.indexOf(card.answer)
        } else {
            options = gameUseCases.generateOptionsByCards(_uiState.value.cards, card).map { it?.answer ?: "<error>" }
            correctIndex = options.indexOf(_uiState.value.card!!.answer)
        }

        _uiState.value = _uiState.value.copy(
            options = options,
            correctIndex = correctIndex,
        )
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiState.value.currentToast.emit(message)
        }
    }

    fun nextTask(result: Boolean) {
        if (result) {
            _uiState.value = _uiState.value.copy(correct = _uiState.value.correct+1)
            if (showCorrectAnswer) {
                showToast("Correct!")
            }
        } else {
            if (showCorrectAnswer) {
                showToast("Incorrect (${_uiState.value.card!!.answer})")
            }
        }

        _uiState.value = _uiState.value.copy(lvl = _uiState.value.lvl+1)

        if (_uiState.value.lvl >= _uiState.value.cards.size) {
            return
        }

        randomizeOptions()
    }

    fun onEvent(event: UiEvent?) {
        when (event) {
            is UiEvent.ChooseOption -> {
                nextTask(event.result)

                if (_uiState.value.lvl == _uiState.value.cards.size) {
                    _uiState.value = _uiState.value.copy(showEndGameDialog = true)
                }
            }

            null -> {}
        }
    }
}