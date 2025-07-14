package com.rombsquare.solocards.presentation.screens.true_false_mode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.domain.usecases.prefs.PrefUseCases
import com.rombsquare.solocards.presentation.screens.true_false_mode.models.UiEvent
import com.rombsquare.solocards.presentation.screens.true_false_mode.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YesNoGameVM @Inject constructor(
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
                cards = cardUseCases.getCardsByFileId(fileId).shuffled().take(count),
                correct = (0..1).random() == 1
            )
            generateCard2()
        }
    }

    fun generateCard2() {
        val currentCard = _uiState.value.cards[_uiState.value.lvl]
        if (_uiState.value.correct) {
            _uiState.value = _uiState.value.copy(card2 = currentCard)
            return
        }

        if (currentCard.fixedOptions) {
            val options = listOf(currentCard.option1, currentCard.option2, currentCard.option3)
            _uiState.value = _uiState.value.copy(card2 = Card(
                question = "",
                answer = options.random(),
                fileId = -1
            ))
            return
        }

        val randomCard = _uiState.value.cards.filter {!it.fixedOptions}.random()
        if (randomCard == currentCard) {
            generateCard2()
        }
        _uiState.value = _uiState.value.copy(card2 = randomCard)
    }

    fun next(userAnswer: Boolean) {
        if (userAnswer == _uiState.value.correct) {
            if (showCorrectAnswer) {
                showToast("Correct!")
            }
            _uiState.value = _uiState.value.copy(score=_uiState.value.score+1)
        } else {
            if (showCorrectAnswer) {
                showToast("Incorrect")
            }
        }

        _uiState.value = _uiState.value.copy(
            lvl=_uiState.value.lvl+1,
            correct = (0..1).random() == 1
        )

        if (_uiState.value.lvl >= _uiState.value.cards.size) {
            return
        }

        generateCard2()
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiState.value.currentToast.emit(message)
        }
    }

    fun onEvent(event: UiEvent?) {
        when (event) {
            is UiEvent.ChooseOption -> {
                if (_uiState.value.lvl == _uiState.value.cards.size) {
                    _uiState.value = _uiState.value.copy(showEndGameDialog = true)
                }

                next(event.option)

                if (_uiState.value.lvl >= _uiState.value.cards.size) {
                    _uiState.value = _uiState.value.copy(showEndGameDialog = true)
                }
            }

            null -> {}
        }
    }
}