package com.rombsquare.solocards.presentation.screens.training_mode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rombsquare.solocards.domain.usecases.cards.CardUseCases
import com.rombsquare.solocards.presentation.screens.training_mode.models.UiEvent
import com.rombsquare.solocards.presentation.screens.training_mode.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingGameVM @Inject constructor(
    private val cardUseCases: CardUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val fileId = savedStateHandle.get<Int>("fileId")!!
    private val count = savedStateHandle.get<Int>("taskCount")!!

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                cards = cardUseCases.getCardsByFileId(fileId).shuffled().take(count)
            )
        }
    }

    fun onEvent(event: UiEvent?) {
        when (event) {
            UiEvent.ShowAnswer -> {
                _uiState.value = _uiState.value.copy(showAnswerDialog=true)
            }

            is UiEvent.ConfirmAnswer -> {
                _uiState.value = _uiState.value.copy(showAnswerDialog=false)
                if (event.state) {
                    _uiState.value = _uiState.value.copy(score = _uiState.value.score+1)
                }

                _uiState.value = _uiState.value.copy(lvl = _uiState.value.lvl+1)

                if (_uiState.value.lvl >= _uiState.value.cards.size) {
                    _uiState.value = _uiState.value.copy(showEndGameDialog = true)
                }
            }

            null -> {}
        }
    }
}