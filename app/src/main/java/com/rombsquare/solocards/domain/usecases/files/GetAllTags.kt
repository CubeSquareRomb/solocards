package com.rombsquare.solocards.domain.usecases.files

import com.rombsquare.solocards.domain.repos.FileRepo

class GetTags(
    private val fileRepo: FileRepo
) {
    suspend operator fun invoke(): List<String> {
        val files = GetFiles(fileRepo).invoke()
        var tags = mutableListOf<String>()

        files.forEach { file ->
            file.tags.forEach { tag ->
                if (tag !in tags) {
                    tags.add(tag)
                }
            }
        }

        return tags.toList()
    }
}