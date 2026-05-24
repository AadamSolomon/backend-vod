package com.vodpass.domain.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class EnsureApplicationRequest(
    val id: Int?,
    @field:Min(value = 0, message = "Version must be >= 0") val version: Int,
    @field:NotBlank(message = "Name is required")
    @field:Length(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    val name: String,
    val email: String?
)
