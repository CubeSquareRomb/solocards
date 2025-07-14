package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.repos.FileRepo

class RestoreFile (
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(id: Int) {
        val file = fileRepo.getById(id)!!
        return fileRepo.update(file.copy(isTrashed = false))
    }
}