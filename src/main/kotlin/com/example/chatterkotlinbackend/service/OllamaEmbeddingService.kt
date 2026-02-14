package com.example.chatterkotlinbackend.service

import org.springframework.ai.document.Document
import org.springframework.ai.embedding.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class OllamaEmbeddingService(
    webClientBuilder: WebClient.Builder,
) : EmbeddingModel {

    @Value("\${spring.ai.ollama.base-url}")
    private lateinit var ollamaUrl: String

    private val client: WebClient by lazy { webClientBuilder.baseUrl(ollamaUrl).build() }

    data class EmbeddingsRequest(val model: String, val input: List<String>)
    data class EmbeddingsResponse(val embeddings: List<List<Float>>)

    override fun call(request: EmbeddingRequest): EmbeddingResponse {
        val texts = request.instructions

        val response = client.post()
            .uri("/api/embed")
            .bodyValue(EmbeddingsRequest(model = "nomic-embed-text", input = texts))
            .retrieve()
            .bodyToMono<EmbeddingsResponse>()
            .block() ?: throw RuntimeException("No embedding from Ollama")

        val embeddings = response.embeddings.mapIndexed { idx, vec ->
            Embedding(vec.toFloatArray(), idx)
        }

        return EmbeddingResponse(embeddings)
    }

    override fun embed(document: Document): FloatArray {
        val textList = listOf(document.text)

        val resp = call(EmbeddingRequest(textList, null))

        return resp.results[0].output.map { it }.toFloatArray()
    }
}
