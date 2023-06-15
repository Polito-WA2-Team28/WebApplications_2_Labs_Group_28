package com.lab5.security.util

import com.final_project.security.exception.SecurityErrorDetails
import com.final_project.security.exception.SecurityException
import com.final_project.ticketing.exception.TicketErrorDetails
import com.final_project.ticketing.exception.TicketException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class SecurityAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SecurityException.UnauthorizedException::class)
    fun unauthorizedError(e: SecurityException.UnauthorizedException): SecurityErrorDetails {
        return SecurityErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SecurityException.UnknownException::class)
    fun unknownError(e: SecurityException.UnknownException): SecurityErrorDetails {
        return SecurityErrorDetails(
            e.error()
        )
    }
}