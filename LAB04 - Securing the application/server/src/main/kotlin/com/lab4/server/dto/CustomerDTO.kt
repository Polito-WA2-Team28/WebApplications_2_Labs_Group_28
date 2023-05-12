package com.lab4.server.dto

import com.lab4.server.model.Customer
import java.util.*

data class CustomerDTO(val id:Long?, val name:String, val surname:String, val registrationDate: Date, val birthDate: Date,
                       val email:String, val phoneNumber:String){

}

fun Customer.toDTO() : CustomerDTO {
    return CustomerDTO(this.getId(), name, surname, registrationDate, birthDate, email, phoneNumber)
}