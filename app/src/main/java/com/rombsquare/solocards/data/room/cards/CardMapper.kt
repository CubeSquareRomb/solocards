package com.rombsquare.solocards.data.room.cards

import com.rombsquare.solocards.domain.models.Card

fun CardEntity.toDomain(): Card =
    Card(
        id = id,
        question = question,
        answer = answer,
        fileId = fileId,
        fixedOptions = fixedOptions,
        option1 = option1,
        option2 = option2,
        option3 = option3,
    )

fun Card.toEntity(): CardEntity =
    CardEntity(
        id = id,
        question = question,
        answer = answer,
        fileId = fileId,
        fixedOptions = fixedOptions,
        option1 = option1,
        option2 = option2,
        option3 = option3,
    )