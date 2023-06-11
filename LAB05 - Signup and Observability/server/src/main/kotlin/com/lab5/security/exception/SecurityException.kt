package com.lab5.security.exception

import com.lab5.server.exception.Exception

open class SecurityException(override val message: String): Throwable() {

    open fun error(): String {
        return message
    }

    class UnauthorizedException(message: String): SecurityException(message)

    class UnknownException(message: String): SecurityException(message)

}