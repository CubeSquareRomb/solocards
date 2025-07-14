package com.rombsquare.solocards.data.room.cards

import com.rombsquare.solocards.domain.models.Card
import com.rombsquare.solocards.domain.repos.CardRepo

class CardRepoImpl(private val dao: CardDao): CardRepo {
    override suspend fun insert(card: Card): Int = dao.insert(card.toEntity()).toInt()
    override suspend fun getAll(): List<Card> = dao.getAll().map { it.toDomain() }
    override suspend fun update(card: Card) = dao.update(card.toEntity())
    override suspend fun delete(card: Card) = dao.delete(card.toEntity())
    override suspend fun getByFileId(fileId: Int): List<Card> = dao.getByFileId(fileId).map { it.toDomain() }
    override suspend fun getById(id: Int): Card = dao.getById(id).toDomain()
    override suspend fun deleteAll() = dao.deleteAll()
}