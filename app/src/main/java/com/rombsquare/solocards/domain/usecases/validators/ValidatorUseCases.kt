package com.rombsquare.solocards.domain.usecases.validators

data class ValidatorUseCases(
    val validateCard: ValidateCard,
    val validateTags: ValidateTags,
    val validateName: ValidateName
)
