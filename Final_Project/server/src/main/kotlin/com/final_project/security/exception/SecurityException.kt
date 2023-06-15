package com.final_project.security.exception

import com.final_project.server.exception.Exception

open class SecurityException(override val message: String): Throwable() {

    open fun error(): String {
        return message
    }

    class UnauthorizedException(message: String): SecurityException(message)

    class UnknownException(message: String): SecurityException(message)

}