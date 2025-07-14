package com.rombsquare.solocards.domain.usecases.prefs

import com.rombsquare.solocards.domain.repos.PrefRepo

class GetFirstRun(
    private val prefRepo: PrefRepo
) {
    operator fun invoke(): Boolean {
        return prefRepo.getFirstRun()
    }
}