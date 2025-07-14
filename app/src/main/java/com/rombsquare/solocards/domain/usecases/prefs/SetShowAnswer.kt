package com.rombsquare.solocards.domain.usecases.prefs

import com.rombsquare.solocards.domain.repos.PrefRepo

class SetShowAnswer(
    private val prefRepo: PrefRepo
) {
    operator fun invoke(state: Boolean) {
        prefRepo.setShowAnswer(state)
    }
}