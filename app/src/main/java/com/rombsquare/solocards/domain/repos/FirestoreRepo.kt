package com.rombsquare.solocards.domain.repos

interface FirestoreRepo {
    suspend fun saveData(uid: String): Boolean
    suspend fun loadData(uid: String): Boolean
    suspend fun createDefaultUserData(uid: String)
    suspend fun subtractAiTokens(uid: String, tokens: Int)
    suspend fun areEnoughTokens(uid: String, tokensToUse: Int): Boolean
    suspend fun refillTokensIfPossible(uid: String)
}