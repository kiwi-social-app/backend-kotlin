package com.example.chatterkotlinbackend

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    @Value("\${app.cors.allowed-origins}")
    lateinit var allowedOrigins: String

    @Value("\${app.cors.allowed-methods}")
    lateinit var allowedMethods: Array<String>

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods(*allowedMethods)
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}