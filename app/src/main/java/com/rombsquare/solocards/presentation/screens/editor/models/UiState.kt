package com.rombsquare.solocards.presentation.screens.editor.models

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.File
import kotlinx.coroutines.flow.MutableSharedFlow

data class UiState(
    val cards: List<Card> = emptyList(),
    val file: File = File(""),
    val chosenCard: Card? = null,
    val chosenMode: Mode? = null,
    val currentToast: MutableSharedFlow<String> = MutableSharedFlow(),
    val action: UiAction? = null
)
