package com.example.chatterkotlinbackend

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Value("\${firebase.project.id}")
    lateinit var firebaseProjectId: String

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf{
                it.disable()
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.GET, "/posts/favorites").authenticated()
                    .requestMatchers(HttpMethod.GET, "/posts/*/is-favorited").authenticated()
                    .requestMatchers(HttpMethod.GET, "/chat/**").authenticated()

                    .requestMatchers(HttpMethod.GET, "/posts", "/posts/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users", "/users/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/comments/**").permitAll()

                    .requestMatchers(HttpMethod.POST, "/users").permitAll()

                    .requestMatchers("/ws", "/ws/**").permitAll()

                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { auth -> auth.jwt{}}

        http.cors { cors -> cors.configurationSource(corsConfigurationSource()) }


        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return JwtDecoders.fromIssuerLocation("https://securetoken.google.com/$firebaseProjectId")
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        return jwtAuthenticationConverter
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("POST", "GET", "PUT", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}