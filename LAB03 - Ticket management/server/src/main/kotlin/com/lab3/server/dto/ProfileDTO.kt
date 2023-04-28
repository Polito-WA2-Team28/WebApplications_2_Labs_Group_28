package com.lab3.server.dto

import com.lab3.server.model.Customer
import java.util.*

data class ProfileDTO(val id:Long?, val name:String, val surname:String, val registrationDate: Date, val birthDate: Date,
                       val email:String, val phoneNumber:String){

}

fun Customer.toDTO() : ProfileDTO {
    return ProfileDTO(this.getId(), name, surname, registrationDate, birthDate, email, phoneNumber)
}