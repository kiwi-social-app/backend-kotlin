package com.example.chatterkotlinbackend

data class EmbeddableDocument(
    val content: String,
    val metadata: Map<String, Any> = emptyMap(),
    val embedding: List<Float>
)
