package com.vodpass.repository

import com.vodpass.domain.entity.Integration
import com.vodpass.domain.entity.enums.Status
import com.vodpass.exception.ResourceNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IntegrationRepository : JpaRepository<Integration, Int> {

    fun requireById(id: Int): Integration =
        findById(id).orElseThrow { ResourceNotFoundException("Integration not found for id") }

    @Query("SELECT COUNT(i) FROM Integration i WHERE i.status = :status")
    fun countByStatus(status: Status): Long

    @Query("SELECT COUNT(i) FROM Integration i")
    fun countAll(): Long

    @Query("""
        SELECT i FROM Integration i
        WHERE (:id IS NULL OR i.id = :id)
        AND (:version IS NULL OR i.version = :version)
        AND (:account IS NULL OR i.account = :account)
        AND (:status IS NULL OR i.status = :status)
        AND (:username IS NULL OR i.username = :username)
        AND (:age IS NULL OR i.age = :age)
        AND (:subType IS NULL OR i.subType = :subType)
        AND (:applicationId IS NULL OR EXISTS (SELECT 1 FROM i.applications a WHERE a.id = :applicationId))
    """)
    fun searchPage(
        id: Int?,
        version: Int?,
        account: String?,
        status: Status?,
        username: String?,
        age: Int?,
        subType: String?,
        applicationId: Int?,
        pageable: Pageable
    ): Page<Integration>
}
