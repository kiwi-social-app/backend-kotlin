package com.example.chatterkotlinbackend.service

import com.example.chatterkotlinbackend.GoogleAuthUser
import com.example.chatterkotlinbackend.dto.UserDTO
import com.example.chatterkotlinbackend.entity.UserEntity
import com.example.chatterkotlinbackend.mapper.UserMapper
import com.example.chatterkotlinbackend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, private val mapper: UserMapper) {

    fun createUser(googleAuthUser: GoogleAuthUser): UserDTO {
        val defaultUsername = generateDefaultUsername(googleAuthUser.email)

        val newUser = UserEntity(
            id = googleAuthUser.uid,
            email = googleAuthUser.email,
            username = defaultUsername
        )
        val createdUser = userRepository.save(newUser)
        return mapper.toDto(createdUser);
    }

    private fun generateDefaultUsername(email: String): String {
        val prefix = email.substringBefore("@").take(5)
        var username = prefix
        var counter = 1

        while (userRepository.existsByUsername(username)) {
            username = "$prefix$counter"
            counter++
        }

        return username
    }
}