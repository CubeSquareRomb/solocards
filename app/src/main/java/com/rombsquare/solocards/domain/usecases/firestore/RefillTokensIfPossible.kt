package com.rombsquare.solocards.domain.usecases.firestore

import com.rombsquare.solocards.domain.repos.FirestoreRepo

class RefillTokensIfPossible(
    private val firestoreRepo: FirestoreRepo
) {
    suspend operator fun invoke(uid: String) {
        firestoreRepo.refillTokensIfPossible(uid)
    }
}