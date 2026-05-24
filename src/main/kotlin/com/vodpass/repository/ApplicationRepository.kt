package com.vodpass.repository

import com.vodpass.domain.entity.Application
import com.vodpass.exception.ResourceNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApplicationRepository : JpaRepository<Application, Int> {

    fun requireById(id: Int): Application =
        findById(id).orElseThrow { ResourceNotFoundException("Application not found for id") }

    @Query("""
        SELECT a FROM Application a
        WHERE (:id IS NULL OR a.id = :id)
        AND (:version IS NULL OR a.version = :version)
        AND (:name IS NULL OR a.name = :name)
        AND (:integrationId IS NULL OR EXISTS (SELECT 1 FROM a.integrations i WHERE i.id = :integrationId))
    """)
    fun searchPage(
        id: Int?,
        version: Int?,
        name: String?,
        integrationId: Int?,
        pageable: Pageable
    ): Page<Application>
}
