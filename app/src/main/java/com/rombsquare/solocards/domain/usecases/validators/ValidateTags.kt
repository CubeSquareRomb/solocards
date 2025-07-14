package com.rombsquare.solocards.domain.usecases.validators

class ValidateTags {
    operator fun invoke(
        tagList: List<String>,
    ): Result {
        tagList.forEach {
            if (it.length > 15) {
                return Result(false, "Some tags are too long")
            }

            if (it in listOf("all", "favorite", "trash")) {
                return Result(false, "Some tags contain special names")
            }

            if (it.isEmpty()) {
                return Result(false, "Some tags are empty")
            }
        }

        return Result(true)
    }

    data class Result(
        val isSuccessful: Boolean,
        val errorMessage: String? = null
    )

}