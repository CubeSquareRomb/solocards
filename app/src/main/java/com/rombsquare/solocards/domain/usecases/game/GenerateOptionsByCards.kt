package com.rombsquare.solocards.domain.usecases.game

import com.rombsquare.solocards.domain.models.Card

// For non-fixed card-answer
class GenerateOptionsByCards {
    operator fun invoke(cards: List<Card>, card: Card): List<Card?> {
        if (card.fixedOptions) {
            return listOf(null, null, null, card).shuffled()
        }

        val fixedCards = cards.filter { it.fixedOptions }
        val (options, _) = GenerateOptions().invoke(
            cards.size,
            4,
            cards.indexOf(card),
            fixedCards.map { cards.indexOf(it) }
        )

        val cardOptions = options.map { cards[it] }

        return cardOptions.shuffled()
    }
}