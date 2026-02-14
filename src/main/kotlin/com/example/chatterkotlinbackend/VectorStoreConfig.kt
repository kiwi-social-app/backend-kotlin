package com.example.chatterkotlinbackend

import com.example.chatterkotlinbackend.service.OllamaEmbeddingService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class VectorStoreConfig {

     @Bean
     fun ollamaEmbeddingService(webClientBuilder: WebClient.Builder): OllamaEmbeddingService {
         return OllamaEmbeddingService(webClientBuilder)
     }

     @Bean
     fun vectorStore(
         dataSource: DataSource,
         embeddingService: OllamaEmbeddingService
     ): PgVectorStore {
        val jdbcTemplate = JdbcTemplate(dataSource)

         return PgVectorStore.builder(jdbcTemplate, embeddingService)
             .initializeSchema(true)
             .dimensions(768)
             .build()
    }
}
