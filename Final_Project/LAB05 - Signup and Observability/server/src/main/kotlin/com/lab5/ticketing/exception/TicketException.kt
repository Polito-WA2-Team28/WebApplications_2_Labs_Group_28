package com.lab5.ticketing.exception

import com.lab5.server.exception.Exception

open class TicketException(override val message: String): Throwable() {

    open fun error(): String {
        return message
    }

    class TicketNotFoundException(message: String): TicketException(message)

    class TicketInvalidOperationException(message: String): TicketException(message)

    class TicketCreationException(message: String): TicketException(message)

    class TicketForbiddenException(message: String): TicketException(message)

    class ValidationException(message: String, private val invalidFields: List<String>): Exception(message) {
        override fun error(): String {
            return "The following fields are invalid: $invalidFields"
        }
    }

}