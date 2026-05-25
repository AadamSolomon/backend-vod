package com.vodpass.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/health").permitAll()
                    .anyRequest().authenticated()
            }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            // allowedOrigins = listOf("http://localhost:5173") // frontend
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }

    @Bean
    fun healthRoute(): RouterFunction<ServerResponse> =
        RouterFunctions.route()
            .GET("/health") { ServerResponse.ok().build() }
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = object : PasswordEncoder {
        override fun encode(rawPassword: CharSequence?): String? {
            rawPassword ?: return null
            val hash = MessageDigest.getInstance("SHA-256")
                .digest(rawPassword.toString().toByteArray(StandardCharsets.UTF_8))
            return hash.joinToString("") { "%02x".format(it) }
        }

        override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean =
            encode(rawPassword) == encodedPassword
    }
}
