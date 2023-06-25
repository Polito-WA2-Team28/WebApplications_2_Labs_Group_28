package com.final_project.server.dto

import com.final_project.server.model.Product
import java.util.UUID

data class ProductDTO(
    val id: Long,
    val serialNumber: UUID,
    val deviceType: String,
    val model: String,
    val registered: Boolean,
    val owner: UUID?
) {}

fun Product.toDTO(): ProductDTO{
    return ProductDTO(id, serialNumber, deviceType, model, registered, owner?.id)
}