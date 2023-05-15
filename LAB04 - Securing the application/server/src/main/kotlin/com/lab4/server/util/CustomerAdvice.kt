package com.lab4.server.util

import com.lab4.server.exception.ErrorDetails
import com.lab4.server.exception.Exception
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.net.ConnectException

@ControllerAdvice
class CustomerAdvice {

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

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Exception.ProfileAlreadyExistingException::class)
    fun profileAlreadyExisting(e: Exception.ProfileAlreadyExistingException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ConnectException::class)
    fun lostServerConnection(e: ConnectException): ErrorDetails {
        return ErrorDetails(
            e.message?: "Unknown server error"
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.CustomerNotFoundException::class)
    fun customerNotFoundError(e: Exception.CustomerNotFoundException): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Exception.CouldNotRegisterCustomer::class)
    fun couldNotRegisterCustomer(e: Exception.CouldNotRegisterCustomer): ErrorDetails {
        return ErrorDetails(
            e.error()
        )
    }


}