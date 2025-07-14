package com.rombsquare.solocards.domain.usecases.firestore

import com.rombsquare.solocards.domain.repos.FirestoreRepo

class CreateDefaultUserData(
    private val firestoreRepo: FirestoreRepo
) {
    suspend operator fun invoke(uid: String) {
        firestoreRepo.createDefaultUserData(uid)
    }
}