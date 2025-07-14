package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.models.File
import com.rombsquare.solocards.domain.repos.FileRepo

class GetFilesByTag(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(tag: String): List<File> {
        val files = GetFiles(fileRepo).invoke()

        return when (tag) {
            "all" -> files.filter { !it.isTrashed }
            "fav" -> files.filter { it.isFav && !it.isTrashed}
            "trash" -> files.filter {it.isTrashed}
            else -> files.filter { it.tags.contains(tag) && !it.isTrashed }
        }
    }
}