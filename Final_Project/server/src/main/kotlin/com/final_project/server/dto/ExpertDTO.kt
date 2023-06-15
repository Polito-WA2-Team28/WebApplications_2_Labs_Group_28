package com.lab5.server.dto

import com.lab5.server.model.Expert
import java.util.*

data class ExpertDTO (
    val id: UUID,
    val email: String
) {}

fun Expert.toDTO(): ExpertDTO {
    return ExpertDTO(id, email)
}