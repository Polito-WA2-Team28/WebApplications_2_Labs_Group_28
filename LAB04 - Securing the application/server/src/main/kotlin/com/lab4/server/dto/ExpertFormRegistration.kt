package com.lab4.server.dto

import com.lab4.ticketing.util.ExpertiseFieldEnum
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class ExpertFormRegistration(
    @field:NotNull
    @field:NotBlank
    val email: String,

    @field:NotNull
    @field:NotBlank
    val expertiseFields: MutableSet<ExpertiseFieldEnum>,
) {
}