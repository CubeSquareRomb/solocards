package com.rombsquare.solocards.domain.repos

interface AiRepo {
    suspend fun callOpenAI(descr: String): String?
}