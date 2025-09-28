package com.example.chatterkotlinbackend.dto

import java.time.OffsetDateTime


class MessageDTO(
    var id: String?,
    val sender: UserBasicDTO,
    val content: String,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val chatId: String,
)