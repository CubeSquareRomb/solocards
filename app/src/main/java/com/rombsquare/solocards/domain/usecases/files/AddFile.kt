package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.FileRepo

class AddFile(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(file: File, selectedTag: String = "all"): Long {
        return when (selectedTag) {
            "all" -> fileRepo.insert(file)
            "fav" -> fileRepo.insert(file.copy(isFav = true))
            else -> fileRepo.insert(file.copy(tags = listOf(selectedTag)))
        }
     }
}