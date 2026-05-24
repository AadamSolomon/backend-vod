package com.vodpass.domain.entity.enums

import com.vodpass.exception.BadRequestException
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

enum class SubType(val value: String) {
    EAS("EAS"),
    ORACLE_DB("Oracle DB"),
    BOT("BOT"),
    SQL_SERVER_DB("SQL Server DB"),
    IDV("IDV"),
    SOAP("SOAP"),
    INTERNAL_API("Internal API"),
    CUR("CUR"),
    ADV("ADV"),
    AD("AD"),
    VF_ROOT("VF-Root"),
    LDAP("LDAP"),
    REST_API("REST API"),
    DB("DB"),
    AMQP("AMQP"),
    TERRADATA_SQL("TerraData SQL"),
    POSTGRES_DB("Postgres DB");

    companion object {
        fun fromValue(value: String?): SubType? {
            if (value.isNullOrBlank()) return null
            val trimmed = value.trim()
            return entries.firstOrNull { it.value.equals(trimmed, ignoreCase = true) || it.name.equals(trimmed, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown SubType: $value")
        }

        fun parseSubTypeFilter(raw: String?): SubType? {
            if (raw.isNullOrBlank()) return null
            return try {
                fromValue(raw.trim())
            } catch (e: IllegalArgumentException) {
                throw BadRequestException(e.message ?: "Unknown SubType")
            }
        }

        fun parseSubTypeRequired(raw: String?): SubType {
            if (raw.isNullOrBlank()) throw BadRequestException("SubType is required")
            return try {
                fromValue(raw.trim()) ?: throw BadRequestException("SubType is required")
            } catch (e: IllegalArgumentException) {
                throw BadRequestException(e.message ?: "Unknown SubType")
            }
        }
    }

    @Converter(autoApply = false)
    class JpaConverter : AttributeConverter<SubType, String> {
        override fun convertToDatabaseColumn(attribute: SubType?): String? = attribute?.value
        override fun convertToEntityAttribute(dbData: String?): SubType? = dbData?.let { fromValue(it) }
    }
}
