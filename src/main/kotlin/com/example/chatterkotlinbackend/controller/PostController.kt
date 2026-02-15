package com.example.chatterkotlinbackend.controller

import com.example.chatterkotlinbackend.dto.PostCreationDTO
import com.example.chatterkotlinbackend.dto.PostDTO
import com.example.chatterkotlinbackend.entity.PostEntity
import com.example.chatterkotlinbackend.entity.UserEntity
import com.example.chatterkotlinbackend.mapper.PostMapper
import com.example.chatterkotlinbackend.repository.PostRepository
import com.example.chatterkotlinbackend.repository.UserRepository
import com.example.chatterkotlinbackend.service.PostService
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/posts")
class PostController {
    @Autowired
    private lateinit var postMapper: PostMapper

    @Autowired
    private lateinit var postService: PostService

    @Autowired
    lateinit var postRepository: PostRepository


    @GetMapping
    fun allPosts(): ResponseEntity<List<PostDTO>> {
        return try {
            val posts: List<PostDTO> = postRepository.findAll().map { postMapper.toDto(it) }
            if (posts.isEmpty()) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(posts, HttpStatus.OK)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emptyList())
        }
    }

    @PostMapping
    fun createPost(
        @RequestBody post: PostCreationDTO,
        principal: Principal
    ): PostDTO {
        val userId = principal.name
        return this.postService.createPost(post, userId)
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: String): ResponseEntity<PostDTO> {
        val postData: Optional<PostEntity> = postRepository.findById(id)
        return if (postData.isPresent) {
            ResponseEntity(postMapper.toDto(postData.get()), HttpStatus.OK)
        } else {
            ResponseEntity(null, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/user/{id}")
    fun getPostsByUser(@PathVariable id: String): ResponseEntity<List<PostDTO>> {
        return try {
            val posts: List<PostDTO> = postRepository.findByAuthorId(id).map { postMapper.toDto(it) }
            if (posts.isEmpty()) {
                ResponseEntity(HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(posts, HttpStatus.OK)
            }
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @PathVariable id: String
    ): ResponseEntity<Unit> {
        return try {
            val post = postRepository.findById(id)
            if (post.isPresent) {
                postRepository.delete(post.get())
                ResponseEntity(null, HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(null, HttpStatus.NOT_FOUND)
            }

        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: String,
        @RequestBody updatedPost: PostEntity
    ): ResponseEntity<PostDTO> {
        return try {
            val existingPost = postRepository.findById(id)
            if (existingPost.isPresent) {
                val postToUpdate = existingPost.get()

                if (postToUpdate.body != updatedPost.body) {
                    postToUpdate.body = updatedPost.body
                }

                val updatedPostEntity = postRepository.save(postToUpdate)
                ResponseEntity(postMapper.toDto(updatedPostEntity), HttpStatus.OK)

            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/{postId}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun favoritePost(
        @PathVariable postId: String,
        principal: Principal
    ) {
        postService.favoritePost(postId, principal.name)
    }

    @DeleteMapping("/{postId}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun unfavoritePost(
        @PathVariable postId: String,
        principal: Principal
    ) = postService.unfavoritePost(postId, principal.name)

    @GetMapping("/favorites")
    fun getUserFavorites(principal: Principal): List<PostDTO> {
      return  postMapper.toDto(postService.getFavoritesByUser(principal.name))
    }

    @GetMapping("/{postId}/is-favorited")
    fun isFavorited(
        @PathVariable postId: String,
        principal: Principal
    ): Boolean = postService.isPostFavoritedByUser(postId, principal.name)

    @PostMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addLike(
        @PathVariable postId: String,
        principal: Principal
    ) {
        postService.addLike(postId, principal.name)
    }

    @DeleteMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeLike(
        @PathVariable postId: String,
        principal: Principal
    ) {
        postService.removeLike(postId, principal.name)
    }


    @PostMapping("/{postId}/dislike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addDislike(
        @PathVariable postId: String,
        principal: Principal
    ) {
        postService.addDislike(postId, principal.name)
    }

    @DeleteMapping("/{postId}/dislike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeDislike(
        @PathVariable postId: String,
        principal: Principal
    ) {
        postService.removeDislike(postId, principal.name)
    }
}
