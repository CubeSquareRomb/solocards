package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.repos.CardRepo

class DeleteAllCards(
    private val repo: CardRepo
) {
    suspend operator fun invoke() {
        repo.deleteAll()
    }
}