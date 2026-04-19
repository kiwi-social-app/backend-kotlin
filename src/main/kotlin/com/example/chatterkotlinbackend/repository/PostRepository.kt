package com.example.chatterkotlinbackend.repository

import com.example.chatterkotlinbackend.entity.PostEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PostRepository : JpaRepository<PostEntity, String> {
    fun findById(id: String?): Optional<PostEntity>
    fun findByAuthorId(id: String?): List<PostEntity>
    fun findAllByOrderByCreatedAtDesc(): List<PostEntity>
    fun findByAuthorIdOrderByCreatedAtDesc(id: String?): List<PostEntity>
}
