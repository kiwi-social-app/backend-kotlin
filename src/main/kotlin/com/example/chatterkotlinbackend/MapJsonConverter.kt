package com.example.chatterkotlinbackend

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class MapJsonConverter : AttributeConverter<Map<String, Any>, String> {
    private val mapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: Map<String, Any>?): String {
        return mapper.writeValueAsString(attribute ?: emptyMap<String, Any>())
    }

    override fun convertToEntityAttribute(dbData: String?): Map<String, Any> {
        if (dbData.isNullOrBlank()) return emptyMap()
        return mapper.readValue(dbData, mapper.typeFactory.constructMapType(Map::class.java, String::class.java, Any::class.java))
    }
}