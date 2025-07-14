package com.rombsquare.solocards.presentation.screens.true_false_mode.models

sealed class UiEvent {
    data class ChooseOption(val option: Boolean) : UiEvent()
}