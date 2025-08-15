package com.example.chatterkotlinbackend

interface EmbeddingService {
    fun embed(text: String): FloatArray
}