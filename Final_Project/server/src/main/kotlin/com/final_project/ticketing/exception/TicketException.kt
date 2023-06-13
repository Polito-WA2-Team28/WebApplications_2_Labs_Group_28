package com.final_project.ticketing.exception

import com.final_project.server.exception.Exception

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