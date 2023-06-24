package com.final_project.server.dto

import com.final_project.server.model.Manager
import java.util.UUID

data class ManagerDTO(
    val id: UUID,
    val email: String
) {}

fun Manager.toDTO(): ManagerDTO {
    return ManagerDTO(id, email)
}