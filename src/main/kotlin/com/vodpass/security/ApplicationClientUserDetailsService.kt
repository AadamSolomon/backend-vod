package com.vodpass.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application")
class ApplicationClientUserDetailsService : UserDetailsService {

    var client: List<Client> = emptyList()

    override fun loadUserByUsername(username: String): UserDetails =
        client.firstOrNull { it.loginName == username }
            ?.let { toUserDetails(it) }
            ?: throw UsernameNotFoundException("User not found")

    private fun toUserDetails(c: Client): UserDetails =
        User.builder()
            .username(c.loginName)
            .password(c.credentials)
            .passwordEncoder { it }
            .disabled(!c.enabled)
            .build()

    class Client {
        var loginName: String = ""
        var credentials: String = ""
        var enabled: Boolean = true
    }
}
