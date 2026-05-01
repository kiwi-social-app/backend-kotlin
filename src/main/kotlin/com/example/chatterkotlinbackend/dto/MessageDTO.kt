package com.example.chatterkotlinbackend.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.OffsetDateTime


class MessageDTO(
    var id: String?,
    val sender: UserBasicDTO,
    val content: String,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val chatId: String,
)