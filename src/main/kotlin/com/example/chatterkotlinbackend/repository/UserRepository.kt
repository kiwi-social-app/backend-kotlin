package com.example.chatterkotlinbackend.repository

import com.example.chatterkotlinbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<UserEntity, String> {
    fun existsByUsername(username: String): Boolean

    @Modifying
    @Query(
        value = "INSERT INTO users (id, email, username) VALUES (:id, :email, :username) ON CONFLICT (id) DO NOTHING",
        nativeQuery = true
    )
    fun insertIfAbsent(id: String, email: String, username: String): Int
}
