package com.example.chatterkotlinbackend.controller

import com.example.chatterkotlinbackend.dto.SearchResultDTO
import com.example.chatterkotlinbackend.repository.PostRepository
import com.example.chatterkotlinbackend.service.SemanticSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController(
    private val semanticSearchService: SemanticSearchService,
    private val postRepository: PostRepository
) {
    @PostMapping("/add")
    fun addDocument(@RequestBody body: Map<String, String>) {
        val content = body["content"] ?: throw IllegalArgumentException("Missing content")
        semanticSearchService.addDocument(content)
    }

    @GetMapping
    fun search(@RequestParam query: String): List<SearchResultDTO> {
        return semanticSearchService.search(query)
    }

    @PostMapping("/index-all")
    fun indexAllPosts() {
        val posts = postRepository.findAll()
        posts.forEach { post ->
            semanticSearchService.addDocument(post.body, post.id)
        }
    }
}