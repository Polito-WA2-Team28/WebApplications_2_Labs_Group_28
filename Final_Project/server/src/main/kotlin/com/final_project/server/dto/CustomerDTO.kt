package com.final_project.server.dto

import com.final_project.server.model.Customer
import java.util.*

data class CustomerDTO(
    val id: UUID, val name:String, val surname:String, val username:String, val registrationDate: Date, val birthDate: Date,
    val email:String, val phoneNumber:String){

}

fun Customer.toDTO() : CustomerDTO {
    return CustomerDTO(this.id, name, surname, username, registrationDate, birthDate, email, phoneNumber)
}