package com.final_project.security.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull

class UserCredentialsDTO(
    @field:NotBlank
    @field:NotNull
    @field:Size(max = 30)
    var username:String,

    @field:NotBlank
    @field:NotNull
    @field:Size(max = 30)
    var password:String
) {
}