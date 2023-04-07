package com.lab2.server.exception

import org.springframework.validation.FieldError
import com.fasterxml.jackson.annotation.JsonProperty

open class Exception(override val message:String) : Throwable() {

    open fun error():String {
        return message
    }

    class ProfileAlreadyExistingException(message:String) : Exception(message)

    class ProductNotFoundException(message:String) : Exception(message)

    class ProfileNotFoundException(message:String) : Exception(message)

    class ValidationException(message:String, private val invalidFields:List<String>) : Exception(message){
        override fun error(): String {
            return "The following fields are invalid: $invalidFields"
        }
    }
}