package com.rombsquare.solocards.domain.repos

interface PrefRepo {
    fun getShowAnswer(): Boolean
    fun setShowAnswer(state: Boolean)
    fun getFirstRun(): Boolean
    fun setFirstRunFalse()
}