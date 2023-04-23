package com.lab3.server.exception


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

    /**
     * Used to throw exceptions related to the unconsistent behaviour of the database. As an example,
     * if the save() JpaRepository method returns an object different from the original one, it is
     * necessary to throw this exception.
     */
    class DatabaseError(message:String) : Exception(message)
}