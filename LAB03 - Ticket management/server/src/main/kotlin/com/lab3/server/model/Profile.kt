package com.lab3.server.model


import com.lab3.server.dto.ProfileFormModification
import com.lab3.server.dto.ProfileFormRegistration
import jakarta.persistence.*
import java.util.*

@Entity
@Table
class Profile (
    var name:String,
    var surname:String,

    @Temporal(value=TemporalType.DATE)
    var registrationDate:Date,

    @Temporal(value=TemporalType.DATE)
    var birthDate:Date,
    var email:String,
    var phoneNumber:String

):EntityBase<Long>() {



}


/**
 * Create a (model) Profile object given the Profile information retrieved from the Profile registration form.
 *
 * @return a Profile object.
 */
fun ProfileFormRegistration.toModel(): Profile{
    return Profile(name, surname, registrationDate, birthDate, email, phoneNumber)
}

/**
 * Create a (model) Profile object given the Profile information retrieved from the Profile modification form.
 * It receives in input the additional information the user could not modify.
 *
 * @param id the identifier of the user retrieved from the database.
 * @param registrationDate the registration date of the user retrieved from the database.
 * @param email the email of the user retrieved from the database.
 * @return a Profile object.
 */
fun ProfileFormModification.toModel(id:Long?, registrationDate: Date, email: String): Profile {
    return Profile(name, surname, registrationDate, birthDate, email, phoneNumber)
}