package com.rombsquare.solocards.domain.usecases.ai

import android.util.Log
import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.AiRepo
import com.rombsquare.solocards.domain.repos.CardRepo
import com.rombsquare.solocards.domain.repos.FileRepo

class GenerateCards(
    private val aiRepo: AiRepo,
    private val fileRepo: FileRepo,
    private val cardRepo: CardRepo
) {
    suspend operator fun invoke(name: String, description: String): Boolean {
        val cardsString = aiRepo.callOpenAI(description)

        if (cardsString == null) {
            Log.e("TESTEST", "Response somehow is null")
            return false
        }

        val cards = cardsString
            .split("\n")
            .map {
                it.split("|").map { it.trim() }
            }

        val id = fileRepo.insert(File(
            name = name
        )).toInt()

        cards.forEach { card ->
            if (card.size != 5 || card[0] == "QUESTION" ) {
                Log.w("TESTEST", "Strange card... (size: ${card.size})")
            } else {
                cardRepo.insert(Card(
                    question = card[0],
                    answer = card[1],
                    fileId = id,
                    fixedOptions = true,
                    option1 = card[2],
                    option2 = card[3],
                    option3 = card[4]
                ))
            }
        }

        return true
    }
}