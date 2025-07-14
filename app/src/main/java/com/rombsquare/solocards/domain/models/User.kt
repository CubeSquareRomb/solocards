package com.rombsquare.solocards.domain.models

data class User(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)