package com.example.chatterkotlinbackend.dto

class SearchResultDTO(
    val content: String,
    val score: Double,
    val postId: String? = null
)