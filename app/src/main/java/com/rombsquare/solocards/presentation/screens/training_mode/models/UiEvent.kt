package com.rombsquare.solocards.presentation.screens.training_mode.models

sealed class UiEvent {
    object ShowAnswer : UiEvent()
    data class ConfirmAnswer(val state: Boolean) : UiEvent()
}