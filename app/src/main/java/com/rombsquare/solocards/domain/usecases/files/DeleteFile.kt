package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.FileRepo

class DeleteFile(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(file: File) {
        fileRepo.delete(file)
    }
}