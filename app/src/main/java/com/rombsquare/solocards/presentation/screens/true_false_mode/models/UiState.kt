package com.rombsquare.solocards.presentation.screens.true_false_mode.models

import com.rombsquare.solocards.domain.models.Card
import kotlinx.coroutines.flow.MutableSharedFlow

data class UiState(
    val lvl: Int = 0,
    val card2: Card? = null, // Comparison card
    val cards: List<Card> = emptyList(),
    val currentToast: MutableSharedFlow<String> = MutableSharedFlow(),
    val score: Int = 0,
    val correct: Boolean = false,
    val showEndGameDialog: Boolean = false
)
