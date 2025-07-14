package com.rombsquare.solocards.data.room.files

import com.rombsquare.solocards.domain.repos.FileRepo
import com.rombsquare.solocards.domain.models.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FileRepoImpl(
    private val fileDao: FileDao
): FileRepo {
    override fun getAll(): Flow<List<File>> {
        return fileDao.getAll()
            .map { entityList ->
                entityList.map { it.toDomain() }
            }
    }

    override suspend fun getById(id: Int): File? {
        return fileDao.getById(id)?.toDomain()
    }

    override suspend fun insert(file: File): Long {
        return fileDao.insert(file.toEntity())
    }

    override suspend fun update(file: File) {
        fileDao.update(file.toEntity())
    }

    override suspend fun delete(file: File) {
        fileDao.delete(file.toEntity())
    }

    override suspend fun clearTrash() {
        fileDao.clearTrash()
    }

    override suspend fun deleteAll() {
        fileDao.deleteAll()
    }
}