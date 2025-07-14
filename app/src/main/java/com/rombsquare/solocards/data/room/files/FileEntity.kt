package com.rombsquare.solocards.data.room.files

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val tags: String = "",
    val isFav: Boolean = false,
    val isTrashed: Boolean = false
)