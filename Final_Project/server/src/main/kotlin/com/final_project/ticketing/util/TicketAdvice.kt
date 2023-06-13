package com.final_project.ticketing.util

import com.final_project.server.exception.ErrorDetails
import com.final_project.server.exception.Exception
import com.final_project.ticketing.exception.TicketErrorDetails
import com.final_project.ticketing.exception.TicketException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class TicketAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TicketException.ValidationException::class)
    fun validationError(e: TicketException.ValidationException): TicketErrorDetails {
        return TicketErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TicketException.TicketNotFoundException::class)
    fun ticketNotFoundError(e: TicketException.TicketNotFoundException): TicketErrorDetails {
        return TicketErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(TicketException.TicketInvalidOperationException::class)
    fun ticketInvalidOperationError(e: TicketException.TicketInvalidOperationException): TicketErrorDetails {
        return TicketErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(TicketException.TicketCreationException::class)
    fun ticketInvalidOperationError(e: TicketException.TicketCreationException): TicketErrorDetails {
        return TicketErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TicketException.TicketForbiddenException::class)
    fun ticketInvalidOperationError(e: TicketException.TicketForbiddenException): TicketErrorDetails {
        return TicketErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.ExpertNotFoundException::class)
    fun expertNotFoundError(e: Exception.ExpertNotFoundException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.ManagerNotFoundException::class)
    fun managerNotFoundError(e: Exception.ManagerNotFoundException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }
}