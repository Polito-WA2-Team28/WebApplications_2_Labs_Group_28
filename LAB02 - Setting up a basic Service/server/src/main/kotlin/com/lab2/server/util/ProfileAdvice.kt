package com.lab2.server.util

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import com.lab2.server.exception.ErrorDetails

@ControllerAdvice
class ProfileAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun validationError(e: IllegalArgumentException): ErrorDetails {
        return ErrorDetails(
            "validation exception for field ${e.message!!}"
        )
    }
}