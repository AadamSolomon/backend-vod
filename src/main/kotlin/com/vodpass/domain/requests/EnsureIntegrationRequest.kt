package com.vodpass.domain.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

data class EnsureIntegrationRequest(
    val id: Int?,
    @field:Min(value = 0, message = "Version must be >= 0") val version: Int,
    @field:NotNull(message = "Expiration date is required") val expirationDate: LocalDateTime?,
    @field:NotBlank(message = "Account is required")
    @field:Length(max = 100, message = "Account must be at most 100 characters")
    val account: String,
    @field:NotBlank(message = "Username is required")
    @field:Length(max = 100, message = "Username must be at most 100 characters")
    val username: String,
    @field:NotBlank(message = "SubType is required") val subType: String
)
