package com.lab4.server.dto

import com.lab4.server.model.Expert
import com.lab4.ticketing.util.ExpertiseFieldEnum
import java.util.*

data class ExpertDTO(
    val id: UUID, val email: String, val expertiseFields:MutableSet<ExpertiseFieldEnum>
)

fun Expert.toDTO() : ExpertDTO {
    return ExpertDTO(this.id, email, expertiseFields)
}