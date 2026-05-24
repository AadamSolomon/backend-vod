package com.vodpass.scheduler

import com.vodpass.domain.entity.Application
import com.vodpass.domain.entity.Integration
import com.vodpass.service.EmailService
import com.vodpass.service.IntegrationService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class IntegrationExpirationRefreshScheduler(
    private val integrationService: IntegrationService,
    private val emailService: EmailService
) {

    @Scheduled(cron = "0 0 0 * * ?", zone = "Africa/Johannesburg")
    fun runOncePerDay() {
        val updated = integrationService.refreshIntegration()
        for (integration in updated) {
            val subject = "%s #%d (%s)".format(integration.status, integration.id, integration.username)
            for (app in integration.applications) {
                app.email?.let { email ->
                    emailService.sendHtmlEmail(email, subject, buildEmailBody(integration, app))
                }
            }
        }
    }

    private fun buildEmailBody(integration: Integration, app: Application): String = """
        <p>Good day,</p>
        <p>
        Please note this <strong>${integration.subType}</strong> account <strong>${integration.username}</strong> ${integration.status} for the following integration <strong>${integration.account}</strong>.
        </p>
        <p>Account age: <strong>${integration.age}</strong></p>
        <p>Affected application:</p>
        <p><strong>${app.name}</strong></p>
        <p>Current status of this integration: <strong>${integration.status}</strong></p>
        <p>Expiration date: <strong>${integration.expirationDate}</strong></p>
        <p>Kind Regards,<br>
        Vodacom Identity and Access Management</p>
        <p><em>Please do not respond to this email; it has been automatically generated.</em></p>
        </body>
        </html>
    """.trimIndent()
}
