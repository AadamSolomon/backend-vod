package com.vodpass.exception

abstract class ApiException(val statusCode: Int, message: String) : RuntimeException(message)
