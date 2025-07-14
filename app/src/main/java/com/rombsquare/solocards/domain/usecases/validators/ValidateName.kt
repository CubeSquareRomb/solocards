package com.rombsquare.solocards.domain.usecases.validators

class ValidateName {
    operator fun invoke(
        name: String
    ): Result {
        if (name.isEmpty()) {
            return Result(false, "Input is empty")
        }

        if (name.length > 25) {
            return Result(false, "Input is too long")
        }

        return Result(true)
    }

    data class Result(
        val isSuccessful: Boolean,
        val errorMessage: String? = null
    )

}