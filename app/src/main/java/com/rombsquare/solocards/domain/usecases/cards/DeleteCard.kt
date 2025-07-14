package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.repos.CardRepo

class DeleteCard(
    private val repo: CardRepo
) {
    suspend operator fun invoke(card: Card) {
        repo.delete(card)
    }
}