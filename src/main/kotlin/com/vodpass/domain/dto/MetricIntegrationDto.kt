package com.vodpass.domain.dto

data class MetricIntegrationDto(
    val total: Int,
    val good: Int,
    val nearingExpiration: Int,
    val expired: Int
)
