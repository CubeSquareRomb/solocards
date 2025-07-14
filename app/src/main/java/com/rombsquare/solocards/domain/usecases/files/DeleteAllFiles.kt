package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.repos.FileRepo

class DeleteAllFiles(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke() {
        fileRepo.deleteAll()
    }
}