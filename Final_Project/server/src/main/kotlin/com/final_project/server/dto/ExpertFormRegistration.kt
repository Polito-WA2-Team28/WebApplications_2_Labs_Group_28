package com.lab5.server.dto


import com.lab5.ticketing.util.ExpertiseFieldEnum
import jakarta.validation.constraints.Pattern
import org.jetbrains.annotations.NotNull

class ExpertFormRegistration(

    @field:NotNull
    val username: String,

    @field:NotNull
    @field:Pattern(regexp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    val email: String,

    @field:NotNull
    val password: String,    /* temporary password, expert will change it */

    @field:NotNull
    var expertiseFields: MutableSet<ExpertiseFieldEnum>
) {

}


