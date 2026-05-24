package com.vodpass.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "application")
class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Column(name = "version", nullable = false)
    var version: Int = 0

    @Column(name = "name", nullable = false)
    var name: String = ""

    @Column(name = "email")
    var email: String? = null

    @ManyToMany
    @JoinTable(
        name = "application_integrations",
        joinColumns = [JoinColumn(name = "application_id")],
        inverseJoinColumns = [JoinColumn(name = "integration_id")]
    )
    var integrations: MutableList<Integration> = mutableListOf()
}
