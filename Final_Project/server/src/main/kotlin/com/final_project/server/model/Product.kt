package com.final_project.server.model

import com.final_project.server.dto.ProductDTO
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table
class Product(
    var id: Long,
    var serialNumber:UUID,
    var deviceType:String,
    var model:String,
    var registered:Boolean,


    @ManyToOne(cascade = [CascadeType.MERGE]) var owner:Customer?,


):EntityBase<Long>() {

}


fun ProductDTO.toModel(owner: Customer): Product{
    return Product(this.id, this.serialNumber, this.deviceType, this.model, this.registered, owner)
}