package com.rombsquare.solocards.domain.usecases.firestore

import com.rombsquare.solocards.domain.repos.FirestoreRepo

class SubtractAiTokens(
    private val firestoreRepo: FirestoreRepo
) {
    suspend operator fun invoke(uid: String, tokens: Int) {
        firestoreRepo.subtractAiTokens(uid, tokens)
    }
}