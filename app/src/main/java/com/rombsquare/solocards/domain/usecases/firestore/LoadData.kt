package com.rombsquare.solocards.domain.usecases.firestore

import com.rombsquare.solocards.domain.repos.FirestoreRepo

class LoadData(
    private val firestoreRepo: FirestoreRepo
) {
    suspend operator fun invoke(uid: String): Boolean {
        return firestoreRepo.loadData(uid)
    }
}