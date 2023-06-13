package com.final_project.server.exception


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

    class CustomerNotFoundException(message: String): Exception(message)
    class ExpertNotFoundException(message: String): Exception(message)
    class ManagerNotFoundException(message: String): Exception(message)

    class CustomerNotOwnerException(message: String): Exception(message)

    class CouldNotRegisterCustomer(message: String): Exception(message)

    class CreateExpertException(message: String): Exception(message)
}