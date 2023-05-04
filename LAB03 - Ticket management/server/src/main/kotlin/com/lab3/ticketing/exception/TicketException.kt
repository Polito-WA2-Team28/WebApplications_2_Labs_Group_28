package com.lab3.ticketing.exception

open class TicketException(override val message: String): Throwable() {

    open fun error(): String {
        return message
    }

    class TicketNotFoundException(message: String): TicketException(message)

    class TicketInvalidOperationException(message: String): TicketException(message)

}