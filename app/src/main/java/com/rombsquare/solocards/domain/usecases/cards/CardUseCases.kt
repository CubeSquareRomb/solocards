package com.rombsquare.solocards.domain.usecases.cards

data class CardUseCases(
    val addCard: AddCard,
    val deleteAllCards: DeleteAllCards,
    val deleteCard: DeleteCard,
    val updateCard: UpdateCard,
    val getCardsByFileId: GetCardsByFileId,
)