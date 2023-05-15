package com.lab4.server.dto



import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.util.*

class CustomerCompleteRegistration(
    var id:UUID,
    var name:String,
    val surname:String,
    val registrationDate: Date,
    val birthDate: Date,
    val email:String,
    val phoneNumber:String
) {

}


fun CustomerFormRegistration.toCompleteCustomer(id: UUID, profile:CustomerFormRegistration): CustomerCompleteRegistration {
    return CustomerCompleteRegistration(id, profile.name, profile.surname, profile.registrationDate,
                                        profile.birthDate, profile.email, profile.phoneNumber)
}
