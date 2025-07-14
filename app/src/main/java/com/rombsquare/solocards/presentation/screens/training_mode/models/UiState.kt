package com.rombsquare.solocards.presentation.screens.training_mode.models

import com.rombsquare.solocards.domain.models.Card
import kotlinx.coroutines.flow.MutableSharedFlow

data class UiState(
    val lvl: Int = 0,
    val cards: List<Card> = emptyList(),
    val currentToast: MutableSharedFlow<String> = MutableSharedFlow(),
    val score: Int = 0,
    val showAnswerDialog: Boolean = false,
    val showEndGameDialog: Boolean = false
)
