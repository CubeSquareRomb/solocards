package com.rombsquare.solocards.presentation.screens.main.models

sealed class Dialog {
    object CreateFile : Dialog()
    object ClearTrash : Dialog()
    object RestoreFile : Dialog()
    object Settings : Dialog()
    object Profile : Dialog()
    object Licence : Dialog()
    object Ai : Dialog()
}