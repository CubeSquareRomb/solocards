package com.rombsquare.solocards.domain.usecases.prefs

import com.rombsquare.solocards.domain.repos.PrefRepo

class SetFirstRunFalse(
    private val prefRepo: PrefRepo
) {
    operator fun invoke() {
        prefRepo.setFirstRunFalse()
    }
}