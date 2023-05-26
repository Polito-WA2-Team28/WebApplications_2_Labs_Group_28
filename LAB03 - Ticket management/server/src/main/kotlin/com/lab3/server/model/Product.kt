package com.lab3.server.model

import com.lab3.server.dto.ProductDTO
import jakarta.persistence.*

import java.util.*

@Entity
@Table
class Product(
    var deviceType:String,
    var model:String,
    var serialNumber:Long,


    @ManyToOne(cascade = [CascadeType.MERGE]) var owner:Customer,


):EntityBase<Long>() {

}


/*fun ProductDTO.toModel(owner: Customer): Product{
    return Product(deviceType, model, serialNumber, owner)
}*/