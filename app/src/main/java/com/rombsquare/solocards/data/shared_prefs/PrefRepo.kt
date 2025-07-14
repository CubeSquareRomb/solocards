package com.rombsquare.solocards.data.shared_prefs

import com.rombsquare.solocards.domain.repos.PrefRepo

class PrefRepoImpl(
    private val prefManager: PrefManager
) : PrefRepo {

    override fun getShowAnswer(): Boolean = prefManager.getShowAnswer()
    override fun setShowAnswer(state: Boolean) = prefManager.setShowAnswer(state)
    override fun getFirstRun(): Boolean = prefManager.getFirstRun()
    override fun setFirstRunFalse() = prefManager.setFirstRunFalse()
}