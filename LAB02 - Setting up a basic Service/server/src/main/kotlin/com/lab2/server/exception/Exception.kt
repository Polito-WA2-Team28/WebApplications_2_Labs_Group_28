package com.lab2.server.exception

open class Exception(message:String) {

    class ProfileAlreadyExistingException(message:String) : Exception(message) {
    }

    class ProductNotFoundException(message:String) : Exception(message){
    }

    class ProfileNotFoundException(message:String) : Exception(message){

    }
}