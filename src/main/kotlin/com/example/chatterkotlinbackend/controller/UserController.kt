package com.example.chatterkotlinbackend.controller

import com.example.chatterkotlinbackend.GoogleAuthUser
import com.example.chatterkotlinbackend.dto.UserDTO
import com.example.chatterkotlinbackend.dto.UserUpdateDTO
import com.example.chatterkotlinbackend.entity.UserEntity
import com.example.chatterkotlinbackend.mapper.UserMapper
import com.example.chatterkotlinbackend.repository.UserRepository
import com.example.chatterkotlinbackend.service.UserRepositoryService
import com.example.chatterkotlinbackend.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userRepositoryService: UserRepositoryService

    @Autowired
    lateinit var mapper: UserMapper;

    @GetMapping
    fun getAllUsers(): List<UserDTO> {
        return mapper.toDto(userRepository.findAll())
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: String): UserDTO {
        return mapper.toDto(userRepositoryService.getUserById(userId))
    }


    @PostMapping
    fun createUser(
        @RequestBody googleAuthUser: GoogleAuthUser,
    ): UserDTO {
        return userService.createUser(googleAuthUser);
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: String,
        @RequestBody updatedUser: UserUpdateDTO,
        request: HttpServletRequest
    ): ResponseEntity<UserEntity> {
        println("Spring sees Content-Type: ${request.contentType}")
        return try {
            val existingUser = userRepository.findById(userId)
            if (existingUser.isPresent) {
                val userToUpdate = existingUser.get()

                if (userToUpdate.email != updatedUser.email) {
                    userToUpdate.email = updatedUser.email
                }
                if (userToUpdate.username != updatedUser.username) {
                    userToUpdate.username = updatedUser.username
                }
                if (userToUpdate.firstname != updatedUser.firstname) {
                    userToUpdate.firstname = updatedUser.firstname
                }
                if (userToUpdate.lastname != updatedUser.lastname) {
                    userToUpdate.lastname = updatedUser.lastname
                }
                val updatedUserEntity = userRepository.save(userToUpdate)
                ResponseEntity(updatedUserEntity, HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }

    }
}