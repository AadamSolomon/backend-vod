package com.vodpass.domain.entity

import com.vodpass.domain.entity.enums.Status
import com.vodpass.domain.entity.enums.SubType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "integration")
class Integration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Column(name = "updated")
    var updated: LocalDateTime? = null

    @Column(name = "expiration_date")
    var expirationDate: LocalDateTime? = null

    @Column(name = "version", nullable = false)
    var version: Int = 0

    @Column(name = "account", nullable = false)
    var account: String = ""

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: Status? = null

    @Column(name = "username", nullable = false)
    var username: String = ""

    @Column(name = "age")
    var age: Int? = null

    @Convert(converter = SubType.JpaConverter::class)
    @Column(name = "sub_type", nullable = false)
    var subType: SubType? = null

    @ManyToMany(mappedBy = "integrations")
    var applications: MutableList<Application> = mutableListOf()
}
