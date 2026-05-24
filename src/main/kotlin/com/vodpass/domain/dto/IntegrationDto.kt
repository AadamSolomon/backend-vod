package com.vodpass.domain.dto

import com.vodpass.domain.entity.enums.Status
import com.vodpass.domain.entity.enums.SubType
import java.time.LocalDateTime

data class IntegrationDto(
    val id: Int?,
    val expirationDate: LocalDateTime?,
    val version: Int,
    val account: String,
    val status: Status?,
    val username: String,
    val age: Int?,
    val subType: SubType?
)
