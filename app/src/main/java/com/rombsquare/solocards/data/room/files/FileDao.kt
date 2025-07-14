package com.rombsquare.solocards.data.room.files

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(file: FileEntity): Long

    @Query("SELECT * FROM files WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FileEntity?

    @Query("SELECT * FROM files")
    fun getAll(): Flow<List<FileEntity>>

    @Update
    suspend fun update(file: FileEntity)

    @Delete
    suspend fun delete(file: FileEntity)

    @Query("DELETE FROM files WHERE isTrashed = 1")
    suspend fun clearTrash()

    @Query("DELETE FROM files")
    suspend fun deleteAll()
}