package com.lab2.server.dto


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Null
import jakarta.validation.constraints.Size
import org.springframework.format.annotation.DateTimeFormat
import java.util.*

interface ProfileFormPOST {}
interface ProfileFormPUT {}

class ProfileForm {
    @field:NotBlank
    @field:NotNull
    @field:Size(max = 30)
    var name:String

    @field:NotBlank
    @field:NotNull
    @field:Size(max = 30)
    val surname:String

    @field:DateTimeFormat
    @field:Null(groups = [ProfileFormPUT::class])
    @field:NotNull(groups = [ProfileFormPOST::class])
    val registrationDate: Date?

    @field:DateTimeFormat
    @field:NotNull
    val birthDate: Date

    @field:Null(groups = [ProfileFormPUT::class])
    @field:NotNull(groups = [ProfileFormPOST::class])
    @field:Pattern(regexp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})", groups = [ProfileFormPOST::class])
    val email: String?

    @field:NotBlank
    @field:NotNull
    @field:Size(max=10)
    val phoneNumber:String

    constructor(name:String, surname:String, registrationDate: Date?, birthDate:Date, email: String?, phoneNumber:String){
        this.name = name
        this.surname = surname
        this.registrationDate = registrationDate
        this.birthDate = birthDate
        this.email = email
        this.phoneNumber = phoneNumber
    }
}
