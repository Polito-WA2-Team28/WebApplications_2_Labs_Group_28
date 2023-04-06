package com.lab2.server.exception

import org.springframework.validation.FieldError

open class Exception(message:String) : Throwable() {

    open fun error(errorMessage:String):String{
        return errorMessage
    }

    class ProfileAlreadyExistingException(message:String) : Exception(message)

    class ProductNotFoundException(message:String) : Exception(message)

    class ProfileNotFoundException(message:String) : Exception(message)

    class ValidationException(message:String, private val invalidFields:MutableList<FieldError>) : Exception(message){
        override fun error(errorMessage: String): String {
            return "The following fields are invalid: $invalidFields"
        }
    }
}