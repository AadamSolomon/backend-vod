package com.vodpass.controller

import com.vodpass.domain.dto.IntegrationDto
import com.vodpass.domain.dto.MetricIntegrationDto
import com.vodpass.domain.dto.PagedResult
import com.vodpass.domain.entity.enums.Status
import com.vodpass.domain.requests.EnsureIntegrationRequest
import com.vodpass.service.IntegrationService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/integration")
class IntegrationController(private val integrationService: IntegrationService) {

    @GetMapping
    fun listIntegrations(
        @RequestParam(required = false) id: Int?,
        @RequestParam(required = false) version: Int?,
        @RequestParam(required = false) account: String?,
        @RequestParam(required = false) status: Status?,
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) age: Int?,
        @RequestParam(required = false) subType: String?,
        @RequestParam(required = false) applicationId: Int?,
        @PageableDefault(size = 20, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<Response<PagedResult<IntegrationDto>>> =
        ResponseEntity.ok(Response(200, "Integrations retrieved successfully",
            integrationService.listIntegrations(id, version, account, status, username, age, subType, applicationId, pageable)))

    @PostMapping
    fun ensureIntegration(@Valid @RequestBody request: EnsureIntegrationRequest): ResponseEntity<Response<IntegrationDto>> =
        ResponseEntity.ok(Response(201, "Integration created successfully", integrationService.ensure(request)))

    @DeleteMapping
    fun deleteIntegration(@RequestParam id: Int): ResponseEntity<Response<Void>> {
        integrationService.deleteById(id)
        return ResponseEntity.ok(Response(204, "Integration successfully deleted", null))
    }

    @GetMapping("/metric")
    fun metricIntegration(): ResponseEntity<Response<MetricIntegrationDto>> =
        ResponseEntity.ok(Response(200, "Integrations metrics retrieved successfully", integrationService.metrics()))

    @PostMapping("/refresh")
    fun refreshIntegrationTable(): ResponseEntity<Response<Void>> {
        integrationService.refreshIntegration()
        return ResponseEntity.ok(Response(200, "Integration table refreshed successfully", null))
    }
}
