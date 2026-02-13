package com.example.chatterkotlinbackend.service

import com.example.chatterkotlinbackend.dto.SearchResultDTO
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.stereotype.Service

@Service
class SemanticSearchService(
    private val vectorStore: PgVectorStore
) {

    fun addDocument(content: String, postId: String? = null) {
        val metadata = mutableMapOf<String, Any>()
        if (postId != null) {
            metadata["postId"] = postId
        }
        val document = Document(content, metadata)
        vectorStore.add(listOf(document))
    }

    fun search(query: String, topK: Int = 5): List<SearchResultDTO> {
        val results = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(0.5)
                .build()
        )

        return results.mapNotNull { doc ->
            val text = doc.text ?: return@mapNotNull null
            val postId = doc.metadata["postId"] as? String
            val score = doc.score?.toDouble() ?: 0.0
            SearchResultDTO(text, score, postId)
        }
    }
}
