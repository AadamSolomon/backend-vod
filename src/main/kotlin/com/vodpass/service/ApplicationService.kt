package com.vodpass.service

import com.vodpass.domain.dto.ApplicationDto
import com.vodpass.domain.dto.MetricApplicationDto
import com.vodpass.domain.dto.PagedResult
import com.vodpass.domain.entity.Application
import com.vodpass.domain.entity.enums.Status
import com.vodpass.domain.requests.EnsureApplicationRequest
import com.vodpass.exception.BadRequestException
import com.vodpass.exception.ResourceNotFoundException
import com.vodpass.repository.ApplicationRepository
import com.vodpass.repository.IntegrationRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val integrationRepository: IntegrationRepository
) {

    @Transactional
    fun ensure(request: EnsureApplicationRequest): ApplicationDto {
        val application = if (request.id != null) applicationRepository.requireById(request.id) else Application()
        application.version = request.version
        application.name = request.name
        application.email = request.email
        return toDto(applicationRepository.save(application))
    }

    fun metrics(): MetricApplicationDto {
        val all = applicationRepository.findAll()
        var expired = 0
        var nearingExpiration = 0
        for (app in all) {
            val hasExpired = app.integrations.any { it.status == Status.EXPIRED }
            val hasNearing = app.integrations.any { it.status == Status.NEARING_EXPIRATION }
            if (hasExpired) expired++
            if (hasNearing) nearingExpiration++
        }
        return MetricApplicationDto(all.size, expired, nearingExpiration)
    }

    @Transactional
    fun deleteById(id: Int) {
        applicationRepository.delete(applicationRepository.requireById(id))
    }

    @Transactional(readOnly = true)
    fun listApplications(id: Int?, version: Int?, name: String?, integrationId: Int?, pageable: Pageable): PagedResult<ApplicationDto> =
        PagedResult.from(applicationRepository.searchPage(id, version, name, integrationId, pageable).map(::toDto))

    @Transactional
    fun addLink(applicationId: Int, integrationId: Int) {
        val application = applicationRepository.requireById(applicationId)
        val integration = integrationRepository.requireById(integrationId)
        if (application.integrations.any { it.id == integrationId }) {
            throw BadRequestException("Application and Integration are already linked")
        }
        application.integrations.add(integration)
        integration.applications.add(application)
    }

    @Transactional
    fun deleteLink(applicationId: Int, integrationId: Int) {
        val application = applicationRepository.requireById(applicationId)
        val integration = integrationRepository.requireById(integrationId)
        if (application.integrations.none { it.id == integrationId }) {
            throw ResourceNotFoundException("Link between Application and Integration does not exist")
        }
        application.integrations.removeIf { it.id == integrationId }
        integration.applications.removeIf { it.id == applicationId }
    }

    companion object {
        fun toDto(a: Application): ApplicationDto =
            ApplicationDto(a.id, a.version, a.name, a.email)
    }
}
