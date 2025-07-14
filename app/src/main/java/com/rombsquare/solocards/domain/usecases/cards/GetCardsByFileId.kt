package com.rombsquare.solocards.domain.usecases.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.repos.CardRepo

class GetCardsByFileId(val repo: CardRepo) {
    suspend operator fun invoke(fileId: Int): List<Card> {
        return repo.getByFileId(fileId)
    }
}