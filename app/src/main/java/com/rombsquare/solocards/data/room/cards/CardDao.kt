package com.rombsquare.solocards.data.room.cards

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(card: CardEntity): Long

    @Query("SELECT * FROM cards")
    suspend fun getAll(): List<CardEntity>

    @Update
    suspend fun update(card: CardEntity)

    @Delete
    suspend fun delete(card: CardEntity)

    @Query("SELECT * FROM cards WHERE fileId = :fileId")
    suspend fun getByFileId(fileId: Int): List<CardEntity>

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getById(id: Int): CardEntity

    @Query("DELETE FROM cards")
    suspend fun deleteAll()
}