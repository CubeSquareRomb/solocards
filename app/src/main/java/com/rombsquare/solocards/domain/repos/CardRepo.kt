package com.rombsquare.solocards.domain.repos

import com.rombsquare.solocards.domain.models.Card

interface CardRepo {
    suspend fun insert(card: Card): Int
    suspend fun getAll(): List<Card>
    suspend fun getById(id: Int): Card?
    suspend fun update(card: Card)
    suspend fun delete(card: Card)
    suspend fun getByFileId(fileId: Int): List<Card>
    suspend fun deleteAll()
}