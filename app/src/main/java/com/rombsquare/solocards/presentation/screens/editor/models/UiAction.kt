package com.rombsquare.solocards.presentation.screens.editor.models

sealed class UiAction {
    data class Route(val route: String) : UiAction()
}