package com.rombsquare.solocards.domain.usecases.firestore

data class FirestoreUseCases(
    val saveData: SaveData,
    val loadData: LoadData,
    val createDefaultUserData: CreateDefaultUserData,
    val subtractAiTokens: SubtractAiTokens,
    val areEnoughTokens: AreEnoughTokens,
    val refillTokensIfPossible: RefillTokensIfPossible
)
