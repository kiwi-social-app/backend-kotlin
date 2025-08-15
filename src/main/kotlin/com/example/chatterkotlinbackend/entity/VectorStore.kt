package com.example.chatterkotlinbackend.entity

import com.example.chatterkotlinbackend.MapJsonConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "vector_store")
data class VectorStore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val content: String,

    @Column(columnDefinition = "jsonb", nullable = false)
    @Convert(converter = MapJsonConverter::class)
    val metadata: Map<String, Any> = emptyMap()
)