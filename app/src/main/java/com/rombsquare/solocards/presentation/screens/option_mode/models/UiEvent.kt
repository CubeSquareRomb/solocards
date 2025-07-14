package com.rombsquare.solocards.presentation.screens.option_mode.models

sealed class UiEvent {
    data class ChooseOption(val result: Boolean) : UiEvent()
}