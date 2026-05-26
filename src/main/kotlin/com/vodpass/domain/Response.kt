package com.vodpass.controller

import com.vodpass.exception.ApiException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

data class Response<T>(
    val status: Int,
    val message: String,
    val result: T?
) {
    companion object {
        fun toResponse(ex: ApiException): ResponseEntity<Response<Void>> {
            val body = Response<Void>(ex.statusCode, ex.message ?: "", null)
            return ResponseEntity.status(ex.statusCode).body(body)
        }

        fun error(statusCode: Int, message: String): ResponseEntity<Response<Void>> {
            val body = Response<Void>(statusCode, message, null)
            return ResponseEntity.status(statusCode).body(body)
        }

        fun error(statusCode: HttpStatusCode, message: String): ResponseEntity<Response<Void>> =
            error(statusCode.value(), message)
    }
}
