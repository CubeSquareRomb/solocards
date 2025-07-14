package com.rombsquare.solocards.data.room.files

import com.rombsquare.solocards.domain.models.File

fun FileEntity.toDomain(): File =
    File(
        id = id,
        name = name,
        tags = tags.split("|"),
        isFav = isFav,
        isTrashed = isTrashed
    )

fun File.toEntity(): FileEntity =
    FileEntity(
        id = id,
        name = name,
        tags = tags?.joinToString("|") ?: "",
        isFav = isFav,
        isTrashed = isTrashed
    )