package com.example.chatterkotlinbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatterKotlinBackendApplication

fun main(args: Array<String>) {
    runApplication<ChatterKotlinBackendApplication>(*args)
}