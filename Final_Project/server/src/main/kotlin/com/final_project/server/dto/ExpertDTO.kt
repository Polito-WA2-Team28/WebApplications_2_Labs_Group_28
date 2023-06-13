package com.final_project.server.dto

import com.final_project.server.model.Expert
import java.util.*

data class ExpertDTO (
    val id: UUID,
    val email: String
) {}

fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(id, email)
}