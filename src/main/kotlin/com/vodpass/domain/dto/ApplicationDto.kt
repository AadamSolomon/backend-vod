package com.vodpass.domain.dto

data class ApplicationDto(
    val id: Int?,
    val version: Int,
    val name: String,
    val email: String?
)
