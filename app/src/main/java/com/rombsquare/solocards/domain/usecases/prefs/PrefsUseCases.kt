package com.rombsquare.solocards.domain.usecases.prefs

data class PrefUseCases(
    val getShowAnswer: GetShowAnswer,
    val setShowAnswer: SetShowAnswer,
    val getFirstRun: GetFirstRun,
    val setFirstRunFalse: SetFirstRunFalse
)
