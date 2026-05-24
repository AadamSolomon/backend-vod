package com.vodpass.domain.entity.enums

import com.vodpass.domain.entity.Integration
import java.time.Duration
import java.time.LocalDateTime

enum class Status {
    GOOD, NEARING_EXPIRATION, EXPIRED;

    companion object {
        fun fromDaysRemaining(daysRemaining: Int, nearingDays: Int): Status = when {
            daysRemaining < 0 -> EXPIRED
            daysRemaining <= nearingDays -> NEARING_EXPIRATION
            else -> GOOD
        }

        fun daysBetween(start: LocalDateTime, end: LocalDateTime): Int =
            Duration.between(start, end).toDays().toInt()

        fun recomputeExpirationDerivedFields(integration: Integration, now: LocalDateTime, nearingDays: Int) {
            val expiration = integration.expirationDate
            if (expiration == null) {
                integration.age = null
                integration.status = GOOD
            } else {
                val daysRemaining = daysBetween(now, expiration)
                integration.age = daysRemaining
                integration.status = fromDaysRemaining(daysRemaining, nearingDays)
            }
        }
    }
}
