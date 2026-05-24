package com.vodpass.controller

import com.vodpass.domain.dto.ApplicationDto
import com.vodpass.domain.dto.MetricApplicationDto
import com.vodpass.domain.dto.PagedResult
import com.vodpass.domain.requests.EnsureApplicationRequest
import com.vodpass.service.ApplicationService
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
@RequestMapping("/application")
class ApplicationController(private val applicationService: ApplicationService) {

    @GetMapping
    fun listApplications(
        @RequestParam(required = false) id: Int?,
        @RequestParam(required = false) version: Int?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) integrationId: Int?,
        @PageableDefault(size = 20, sort = ["id"]) page: Pageable
    ): ResponseEntity<Response<PagedResult<ApplicationDto>>> =
        ResponseEntity.ok(Response(200, "Applications retrieved successfully",
            applicationService.listApplications(id, version, name, integrationId, page)))

    @PostMapping
    fun ensureApplication(@Valid @RequestBody request: EnsureApplicationRequest): ResponseEntity<Response<ApplicationDto>> =
        ResponseEntity.ok(Response(201, "Application created successfully", applicationService.ensure(request)))

    @DeleteMapping
    fun deleteApplication(@RequestParam id: Int): ResponseEntity<Response<Void>> {
        applicationService.deleteById(id)
        return ResponseEntity.ok(Response(204, "Application successfully deleted", null))
    }

    @GetMapping("/metric")
    fun metricApplication(): ResponseEntity<Response<MetricApplicationDto>> =
        ResponseEntity.ok(Response(200, "Application metrics retrieved successfully", applicationService.metrics()))

    @PostMapping("/link")
    fun addLink(@RequestParam applicationId: Int, @RequestParam integrationId: Int): ResponseEntity<Response<Void>> {
        applicationService.addLink(applicationId, integrationId)
        return ResponseEntity.ok(Response(201, "Link added successfully", null))
    }

    @DeleteMapping("/link")
    fun deleteLink(@RequestParam applicationId: Int, @RequestParam integrationId: Int): ResponseEntity<Response<Void>> {
        applicationService.deleteLink(applicationId, integrationId)
        return ResponseEntity.ok(Response(204, "Link deleted successfully", null))
    }
}
