package com.rombsquare.solocards.presentation.screens.option_mode.models

import com.rombsquare.solocards.domain.models.Card
import kotlinx.coroutines.flow.MutableSharedFlow

data class UiState(
    val lvl: Int = 0,
    val cards: List<Card> = emptyList(),
    val options: List<String> = emptyList(),
    val currentToast: MutableSharedFlow<String> = MutableSharedFlow(),
    val correct: Int = 0,
    val correctIndex: Int = 0,
    val showEndGameDialog: Boolean = false
) {
    val card: Card?
        get() = cards.getOrNull(lvl)
}
