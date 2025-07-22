package com.example.chatterkotlinbackend.repository

import com.example.chatterkotlinbackend.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun existsByUsername(username: String): Boolean
}