package com.rombsquare.solocards.domain.repos

import com.rombsquare.solocards.domain.models.File
import kotlinx.coroutines.flow.Flow

interface FileRepo {
    suspend fun insert(file: File): Long
    fun getAll(): Flow<List<File>>
    suspend fun getById(id: Int): File?
    suspend fun update(file: File)
    suspend fun delete(file: File)
    suspend fun clearTrash()
    suspend fun deleteAll()
}