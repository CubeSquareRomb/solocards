package com.rombsquare.solocards.domain.usecases.validators

import com.rombsquare.solocards.domain.models.Card

class ValidateCard {
    operator fun invoke(
        card: Card,
        cards: List<Card>, // The list of ALL cards in a group (file). It required to avoid cards with the same answer
        editMode: Boolean,
    ): Result {
        if (card.question.isEmpty()) {
            return Result(false, "The question is empty")
        }

        if (card.answer.isEmpty()) {
            return Result(false, "The answer is empty")
        }

        if (card.fixedOptions && (card.option1.isEmpty() || card.option2.isEmpty() || card.option3.isEmpty())) {
            return Result(false, "Some options are empty")
        }

        if (card.question.length > 50) {
            return Result(false, "The question is too long")
        }

        if (card.answer.length > 25) {
            return Result(false, "The answer is too long")
        }

        if (card.fixedOptions && (card.option1.length > 25 || card.option2.length > 25 || card.option3.length > 25)) {
            return Result(false, "One of the fixed options is too long")
        }

        // Throw error, if other existing card has the same answer
        if (
            (cards.count { it.answer == card.answer } >= 1 && !editMode) ||
            (cards.count { it.answer == card.answer } >= 2 && editMode)
        ) {
            return Result(false, "Two cards can't have the same answer")
        }

        return Result(true)
    }

    data class Result(
        val isSuccess: Boolean,
        val errorMessage: String? = null
    )
}