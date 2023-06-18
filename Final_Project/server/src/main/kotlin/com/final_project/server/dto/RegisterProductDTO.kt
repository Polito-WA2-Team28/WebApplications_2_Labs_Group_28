package com.final_project.server.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull
import java.util.UUID

class RegisterProductDTO(
    @field:NotBlank
    @field:NotNull
    @field:Size(max = 10)
    val productId: Long,

    @field:NotBlank
    @field:NotNull
    @field:Size(max = 50)
    val serialNumber: UUID
) {
}