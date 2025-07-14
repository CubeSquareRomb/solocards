package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.repos.FileRepo

class ClearTrash(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke() {
        fileRepo.clearTrash()
    }
}