package com.example.chatterkotlinbackend

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class FloatListJsonConverter : AttributeConverter<List<Float>, String> {
    private val mapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<Float>?): String {
        return mapper.writeValueAsString(attribute ?: emptyList<Float>())
    }

    override fun convertToEntityAttribute(dbData: String?): List<Float> {
        if (dbData.isNullOrBlank()) return emptyList()
        return mapper.readValue(dbData, mapper.typeFactory.constructCollectionType(List::class.java, Float::class.java))
    }
}