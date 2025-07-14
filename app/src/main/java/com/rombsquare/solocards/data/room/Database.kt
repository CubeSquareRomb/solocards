package com.rombsquare.quiz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rombsquare.solocards.data.room.cards.CardDao
import com.rombsquare.solocards.data.room.cards.CardEntity
import com.rombsquare.solocards.data.room.files.FileDao
import com.rombsquare.solocards.data.room.files.FileEntity

@Database(
    entities = [CardEntity::class, FileEntity::class],
    version = 1
)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun fileDao(): FileDao
}