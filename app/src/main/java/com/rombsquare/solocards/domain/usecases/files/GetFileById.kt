package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.FileRepo

class GetFileById(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(id: Int): File? {
        return fileRepo.getById(id)
    }
}