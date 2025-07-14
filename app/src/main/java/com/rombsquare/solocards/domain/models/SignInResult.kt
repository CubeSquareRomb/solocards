package com.rombsquare.solocards.domain.models

data class SignInResult(
    val data: User?,
    val errorMessage: String?,
    val isNewUser: Boolean? = null
)