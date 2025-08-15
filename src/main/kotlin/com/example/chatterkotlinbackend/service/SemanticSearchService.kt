package com.example.chatterkotlinbackend.service

import OllamaEmbeddingService
import com.example.chatterkotlinbackend.dto.SearchResultDTO
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.stereotype.Service

@Service
class SemanticSearchService(
    private val vectorStore: PgVectorStore,
    private val embeddingService: OllamaEmbeddingService
) {

    // Add a document with its embedding
    fun addDocument(content: String, metadata: Map<String, Any> = emptyMap()) {
        val embedding = embeddingService.embed(content)
        val docMetadata = metadata.toMutableMap()
        docMetadata["embedding"] = embedding.toList()

        val document = Document(content, docMetadata)
        vectorStore.add(listOf(document))
    }

    fun search(query: String, topK: Int = 5): List<SearchResultDTO> {
        val queryEmbedding: FloatArray = try {
            embeddingService.embed(query)
        } catch (e: Exception) {
            throw RuntimeException("Failed to embed query", e)
        }

        val allDocs = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(1000)
                .build()
        )

        return allDocs
            .mapNotNull { doc ->
                val text = doc.text ?: return@mapNotNull null  // skip documents with null text
                val embedding = doc.metadata["embedding"] as? FloatArray ?: floatArrayOf()
                SearchResultDTO(text, cosineSimilarity(embedding, queryEmbedding))
            }
            .sortedByDescending { it.score }
            .take(topK)
    }

    private fun cosineSimilarity(a: FloatArray, b: FloatArray): Double {
        val dot = a.zip(b).sumOf { (x, y) -> x.toDouble() * y.toDouble() }
        val normA = kotlin.math.sqrt(a.sumOf { it.toDouble() * it.toDouble() })
        val normB = kotlin.math.sqrt(b.sumOf { it.toDouble() * it.toDouble() })
        return dot / (normA * normB)
    }
}
