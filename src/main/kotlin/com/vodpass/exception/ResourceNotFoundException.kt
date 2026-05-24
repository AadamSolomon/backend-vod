package com.vodpass.exception

import org.springframework.http.HttpStatus

class ResourceNotFoundException(message: String) : ApiException(HttpStatus.NOT_FOUND.value(), message)
