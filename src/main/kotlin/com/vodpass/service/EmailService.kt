package com.vodpass.service

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailPreparationException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @param:Value("\${spring.mail.username}") private val fromAddress: String
) {

    fun sendHtmlEmail(to: String, subject: String, htmlBody: String) {
        try {
            // replace with the vodacom email service (http post request)
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, false, StandardCharsets.UTF_8.name())
            helper.setFrom(fromAddress)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(htmlBody, true)
            mailSender.send(message)
        } catch (e: MessagingException) {
            throw MailPreparationException("Failed to prepare HTML email", e)
        }
    }
}
