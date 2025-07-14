package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.FileRepo
import kotlinx.coroutines.flow.first

class GetFiles(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(): List<File> {
        return fileRepo.getAll().first()
    }
}