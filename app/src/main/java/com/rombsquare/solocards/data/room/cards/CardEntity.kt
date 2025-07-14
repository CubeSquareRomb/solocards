package com.rombsquare.solocards.data.room.cards

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rombsquare.solocards.data.room.files.FileEntity

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = FileEntity::class,
            parentColumns = ["id"],
            childColumns = ["fileId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["fileId"])]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val question: String,
    val answer: String,
    val fileId: Int,
    val fixedOptions: Boolean = false,
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
)