package com.example.chatterkotlinbackend

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets


@Configuration
class WebConfig : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val jacksonConverter = MappingJackson2HttpMessageConverter()
        jacksonConverter.supportedMediaTypes = listOf(
            MediaType("application", "json"),
            MediaType("application", "json", StandardCharsets.UTF_8),
            MediaType("application", "*+json")
        )
        converters.add(0, jacksonConverter)
    }
}