package com.lab2.server.dto

import com.lab2.server.model.Customer
import java.util.*

data class CustomerDTO(val name:String, val surname:String, val registrationDate: Date, val birthDate: Date,
                       val email:String, val phoneNumber:String){

}


fun Customer.toDTO() : CustomerDTO {
    return CustomerDTO(name, surname, registrationDate, birthDate, email, phoneNumber)
}