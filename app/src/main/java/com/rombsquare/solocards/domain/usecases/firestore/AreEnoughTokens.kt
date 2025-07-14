package com.rombsquare.solocards.domain.usecases.firestore

import com.rombsquare.solocards.domain.repos.FirestoreRepo

class AreEnoughTokens(
    private val firestoreRepo: FirestoreRepo
) {
    suspend operator fun invoke(uid: String, tokensToUse: Int): Boolean {
        return firestoreRepo.areEnoughTokens(uid, tokensToUse)
    }
}