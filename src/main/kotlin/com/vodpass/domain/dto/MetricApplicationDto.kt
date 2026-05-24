package com.vodpass.domain.dto

data class MetricApplicationDto(
    val total: Int,
    val withExpiredIntegrations: Int,
    val withNearingExpirationIntegrations: Int
)
