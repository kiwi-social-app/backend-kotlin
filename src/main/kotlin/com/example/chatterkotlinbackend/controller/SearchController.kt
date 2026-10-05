package com.example.chatterkotlinbackend.controller

import com.example.chatterkotlinbackend.dto.SearchResultDTO
import com.example.chatterkotlinbackend.repository.PostRepository
import com.example.chatterkotlinbackend.service.SemanticSearchService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/search")
class SearchController(
    private val semanticSearchService: SemanticSearchService,
    private val postRepository: PostRepository,
    @Value("\${app.admin-uids:}") private val adminUids: List<String>
) {
    @GetMapping
    fun search(@RequestParam query: String): List<SearchResultDTO> {
        return semanticSearchService.search(query)
    }

    // Admin-only: re-embeds every post. Not idempotent yet — each run adds duplicate
    // entries — so only use it on an empty or freshly cleared vector store.
    @PostMapping("/index-all")
    fun indexAllPosts(principal: Principal): ResponseEntity<Unit> {
        if (principal.name !in adminUids) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        val posts = postRepository.findAll()
        posts.forEach { post ->
            semanticSearchService.addDocument(post.body, post.id)
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
