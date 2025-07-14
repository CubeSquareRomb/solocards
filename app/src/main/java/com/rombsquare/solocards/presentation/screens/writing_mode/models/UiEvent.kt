package com.rombsquare.solocards.presentation.screens.writing_mode.models

sealed class UiEvent {
    data class ConfirmAnswer(val userAnswer: String) : UiEvent()
}