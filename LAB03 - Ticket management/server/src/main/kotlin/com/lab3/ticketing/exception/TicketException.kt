package com.lab3.ticketing.exception

import com.lab3.server.exception.Exception

open class TicketException(override val message: String): Throwable() {

    open fun error(): String {
        return message
    }

    class TicketNotFoundException(message: String): TicketException(message)

    class TicketInvalidOperationException(message: String): TicketException(message)

    class ValidationException(message: String, private val invalidFields: List<String>): Exception(message) {
        override fun error(): String {
            return "The following fields are invalid: $invalidFields"
        }
    }

}