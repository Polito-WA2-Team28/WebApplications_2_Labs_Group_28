package com.lab2.server.dto

import com.lab2.server.model.Profile
import java.util.*

data class ProfileDTO(val name:String, val surname:String, val registrationDate: Date, val birthDate: Date,
                       val email:String, val phoneNumber:String){

}


fun Profile.toDTO() : ProfileDTO {
    return ProfileDTO(name, surname, registrationDate, birthDate, email, phoneNumber)
}