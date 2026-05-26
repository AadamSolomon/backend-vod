package com.vodpass.controller.handler

import com.vodpass.controller.Response
import com.vodpass.exception.ApiException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<Response<Void>> =
        Response.toResponse(ex)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Response<Void>> {
        val errorMessage = ex.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "Validation failed."
        return Response.error(HttpStatus.BAD_REQUEST, errorMessage)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(ex: MissingServletRequestParameterException): ResponseEntity<Response<Void>> =
        Response.error(HttpStatus.BAD_REQUEST, "Required parameter '${ex.parameterName}' is missing")

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<Response<Void>> =
        Response.error(HttpStatus.BAD_REQUEST, "Invalid value for parameter '${ex.name}'")

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadableBody(ex: HttpMessageNotReadableException): ResponseEntity<Response<Void>> {
        log.debug("Malformed request body: {}", ex.message)
        return Response.error(HttpStatus.BAD_REQUEST, "Invalid request body")
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupported(ex: HttpRequestMethodNotSupportedException): ResponseEntity<Response<Void>> =
        Response.error(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed for this URL")

    @ExceptionHandler(NoHandlerFoundException::class, NoResourceFoundException::class)
    fun handleNotFound(): ResponseEntity<Response<Void>> =
        Response.error(HttpStatus.NOT_FOUND, "Not found")

    @ExceptionHandler(Exception::class)
    fun handleUnhandled(ex: Exception): ResponseEntity<Response<Void>> {
        log.error("Unhandled exception", ex)
        return Response.error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred")
    }
}
