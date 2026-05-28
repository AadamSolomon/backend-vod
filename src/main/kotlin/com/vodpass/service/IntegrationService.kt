package com.vodpass.service

import com.vodpass.domain.dto.IntegrationDto
import com.vodpass.domain.dto.MetricIntegrationDto
import com.vodpass.domain.dto.PagedResult
import com.vodpass.domain.entity.Integration
import com.vodpass.domain.entity.enums.Status
import com.vodpass.domain.requests.EnsureIntegrationRequest
import com.vodpass.repository.IntegrationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class IntegrationService(
    private val integrationRepository: IntegrationRepository,
    @param:Value("\${near_expiration_days}") private val nearExpirationDays: Int
) {

    @Transactional
    fun ensure(request: EnsureIntegrationRequest): IntegrationDto {
        val integration = if (request.id != null) integrationRepository.requireById(request.id) else Integration()
        val now = LocalDateTime.now()
        integration.updated = now
        integration.expirationDate = request.expirationDate
        integration.version = request.version
        integration.account = request.account
        integration.username = request.username
        integration.subType = request.subType
        Status.recomputeExpirationDerivedFields(integration, now, nearExpirationDays)
        return toDto(integrationRepository.save(integration))
    }

    @Transactional
    fun deleteById(id: Int) {
        integrationRepository.delete(integrationRepository.requireById(id))
    }

    @Transactional(readOnly = true)
    fun metrics(): MetricIntegrationDto {
        val total = integrationRepository.countAll()
        val good = integrationRepository.countByStatus(Status.GOOD)
        val nearingExpiration = integrationRepository.countByStatus(Status.NEARING_EXPIRATION)
        val expired = integrationRepository.countByStatus(Status.EXPIRED)
        return MetricIntegrationDto(total.toInt(), good.toInt(), nearingExpiration.toInt(), expired.toInt())
    }

    @Transactional(readOnly = true)
    fun listIntegrations(
        id: Int?, version: Int?, account: String?, status: Status?,
        username: String?, age: Int?, subType: String?, applicationId: Int?, pageable: Pageable
    ): PagedResult<IntegrationDto> =
        PagedResult.from(
            integrationRepository.searchPage(id, version, account, status, username, age, subType, applicationId, pageable)
                .map(::toDto)
        )

    @Transactional
    fun refreshIntegration(): List<Integration> {
        val now = LocalDateTime.now()
        val all = integrationRepository.findAll()
        val expiredOrNearing = mutableListOf<Integration>()
        for (integration in all) {
            Status.recomputeExpirationDerivedFields(integration, now, nearExpirationDays)
            if (integration.status == Status.EXPIRED || integration.status == Status.NEARING_EXPIRATION) {
                expiredOrNearing.add(integration)
            }
        }
        integrationRepository.saveAll(all)
        for (integration in expiredOrNearing) {
            integration.applications.size // force lazy load
        }
        return expiredOrNearing
    }

    companion object {
        fun toDto(i: Integration): IntegrationDto =
            IntegrationDto(i.id, i.expirationDate, i.version, i.account, i.status, i.username, i.age, i.subType)
    }
}
