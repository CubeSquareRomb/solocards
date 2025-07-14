package com.rombsquare.solocards.domain.models

data class Card(
    val question: String = "",
    val answer: String = "",
    val fileId: Int = -1,
    val fixedOptions: Boolean = false,
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val id: Int? = null
)
