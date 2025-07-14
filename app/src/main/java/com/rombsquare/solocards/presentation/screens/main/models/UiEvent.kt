package com.rombsquare.solocards.presentation.screens.main.models

import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.models.User

sealed class UiEvent {
    object LoadFiles : UiEvent()
    object ShowCreateFileDialog : UiEvent()
    object OpenDrawer : UiEvent()
    object CloseDrawer : UiEvent()
    object ClearTrash : UiEvent()
    object ShowClearTrashDialog : UiEvent()
    object OpenSettings : UiEvent()
    object RestoreFile : UiEvent()
    object OpenProfileDialog : UiEvent()
    object SignOut : UiEvent()
    object SaveData : UiEvent()
    object LoadData : UiEvent()
    object ShowLicenceDialog : UiEvent()
    object ClickAiMode : UiEvent()

    data class SelectFile(val file: File) : UiEvent()
    data class CreateFile(val name: String): UiEvent()
    data class ChooseTag(val tag: String): UiEvent()
    data class SetShowAnswer(val state: Boolean): UiEvent()
    data class SignIn(val user: User) : UiEvent()
    data class GenerateWithAi(val name: String, val descr: String): UiEvent()
}