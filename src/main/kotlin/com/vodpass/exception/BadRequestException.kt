package com.vodpass.exception

import org.springframework.http.HttpStatus

class BadRequestException(message: String) : ApiException(HttpStatus.BAD_REQUEST.value(), message)
