package com.final_project.server.model


import com.final_project.server.dto.CustomerCompleteRegistration
import com.final_project.server.dto.CustomerDTO
import com.final_project.server.dto.CustomerFormModification
import jakarta.persistence.*
import java.util.*

@Entity
@Table
class Customer (
    @Id var id:UUID,
    var name:String,
    var surname:String,
    var username:String,

    @Temporal(value=TemporalType.DATE)
    var registrationDate: Date,

    @Temporal(value=TemporalType.DATE)
    var birthDate:Date,
    var email:String,
    var phoneNumber:String
)


/**
 * Create a (model) Customer object given the Customer information retrieved from the Customer registration form.
 *
 * @return a Customer object.
 */
fun CustomerCompleteRegistration.toModel(): Customer{
    return Customer(id, name, surname, username, registrationDate, birthDate, email, phoneNumber)
}

/**
 * Create a (model) Customer object given the Customer information retrieved from the Customer modification form.
 * It receives in input the additional information the user could not modify.
 *
 * @param id the identifier of the user retrieved from the database.
 * @param registrationDate the registration date of the user retrieved from the database.
 * @param email the email of the user retrieved from the database.
 * @return a Customer object.
 */
fun CustomerFormModification.toModel(id: UUID, username:String, registrationDate: Date, email: String): Customer {
    return Customer(id, name, surname, username, registrationDate, birthDate, email, phoneNumber)
}


fun CustomerDTO.toModel():Customer{
    return Customer(this.id, this.name, this.surname, this.username, this.registrationDate, this.birthDate, this.email, this.phoneNumber)
}