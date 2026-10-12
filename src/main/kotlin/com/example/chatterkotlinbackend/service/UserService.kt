package com.example.chatterkotlinbackend.service

import com.example.chatterkotlinbackend.dto.UserDTO
import com.example.chatterkotlinbackend.mapper.UserMapper
import com.example.chatterkotlinbackend.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository, private val mapper: UserMapper) {

    @Transactional
    fun getOrCreateUser(uid: String, email: String): UserDTO {
        userRepository.findByIdOrNull(uid)?.let { return mapper.toDto(it) }

        userRepository.insertIfAbsent(uid, email, generateDefaultUsername(email))
        return mapper.toDto(userRepository.findById(uid).orElseThrow())
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
