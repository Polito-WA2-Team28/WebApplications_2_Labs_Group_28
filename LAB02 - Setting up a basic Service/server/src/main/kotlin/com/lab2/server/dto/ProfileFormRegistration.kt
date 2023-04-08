package com.lab2.server.dto


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.util.*

class ProfileFormRegistration(
    @field:NotBlank
    @field:NotNull
    @field:Size(max = 30)
    var name:String,

    @field:NotBlank
    @field:NotNull
    @field:Size(max = 30)
    val surname:String,

    @field:DateTimeFormat
    @field:NotNull
    val registrationDate: Date,

    @field:DateTimeFormat
    @field:NotNull
    val birthDate: Date,

    @field:NotNull
    @field:Pattern(regexp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    val email:String,

    @field:NotBlank
    @field:NotNull
    @field:Size(max=10)
    val phoneNumber:String
) {

}
