package com.lab2.server.util

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import com.lab2.server.exception.ErrorDetails
import com.lab2.server.exception.Exception

@ControllerAdvice
class ProfileAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.ProductNotFoundException::class)
    fun productNotFoundError(e: Exception.ProductNotFoundException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.ValidationException::class)
    fun validationError(e: Exception.ValidationException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.ProfileNotFoundException::class)
    fun profileNotFoundError(e: Exception.ProfileNotFoundException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }

}