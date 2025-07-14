package com.rombsquare.solocards.domain.models

data class File(
    val name: String = "",
    val tags: List<String> = emptyList(),
    val isFav: Boolean = false,
    val isTrashed: Boolean = false,
    val id: Int? = null,
)
