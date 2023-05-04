package com.lab3.ticketing.exception

open class Exception(override val message: String): Throwable() {

    open fun error(): String {
        return message
    }

    class TicketNotFoundException(message: String): Exception(message)

    class TicketInvalidOperationException(message: String): Exception(message)

}