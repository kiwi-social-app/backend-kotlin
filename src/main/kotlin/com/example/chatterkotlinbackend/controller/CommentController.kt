package com.example.chatterkotlinbackend.controller

import com.example.chatterkotlinbackend.dto.CommentCreationDTO
import com.example.chatterkotlinbackend.dto.CommentDTO
import com.example.chatterkotlinbackend.mapper.CommentMapper
import com.example.chatterkotlinbackend.repository.CommentRepository
import com.example.chatterkotlinbackend.service.CommentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/comments")
class CommentController {
    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var commentMapper: CommentMapper;


    @PostMapping
    fun createComment(
        @RequestBody comment: CommentCreationDTO,
        @RequestParam postId: String,
        principal: Principal
    ): CommentDTO {
        return commentService.createComment(comment, principal.name, postId)
    }

    @GetMapping("/{postId}")
    fun getCommentsByPost(
        @PathVariable("postId") postId: String
    ): ResponseEntity<List<CommentDTO>> {
        return try {
            val comments: List<CommentDTO> = commentRepository.findByPostId(postId).map { commentMapper.toDto(it) }

            if (comments.isEmpty()) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(comments, HttpStatus.OK)
            }
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}