package com.rombsquare.solocards.domain.usecases.firestore

import com.rombsquare.solocards.domain.repos.FirestoreRepo

class SaveData(
    private val firestoreRepo: FirestoreRepo
) {
    suspend operator fun invoke(uid: String): Boolean {
        return firestoreRepo.saveData(uid)
    }
}