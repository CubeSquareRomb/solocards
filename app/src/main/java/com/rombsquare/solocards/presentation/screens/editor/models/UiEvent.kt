package com.rombsquare.solocards.presentation.screens.editor.models

import com.rombsquare.solocards.domain.models.Card

sealed class UiEvent {
    data class SelectCard(val card: Card) : UiEvent()
    data class CreateCard(val question: String, val answer: String) : UiEvent()
    data class UpdateCard(val card: Card) : UiEvent()
    data class DeleteCard(val card: Card) : UiEvent()
    data class UpdateTags(val tags: List<String>) : UiEvent()
    data class RenameFile(val newName: String) : UiEvent()
    data class ChooseMode(val mode: Mode) : UiEvent()
    data class ConfirmTaskCount(val count: Int) : UiEvent()

    object ClickAddCard : UiEvent()
    object ClickTagIcon : UiEvent()
    object ClickEditIcon : UiEvent()
    object ClickFavoriteIcon : UiEvent()
    object ClickDeleteIcon : UiEvent()
    object DeleteFile : UiEvent()
    object ClickPlayFab : UiEvent()
    object ChooseAllCards : UiEvent()
}