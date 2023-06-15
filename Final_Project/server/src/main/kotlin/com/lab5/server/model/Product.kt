package com.lab5.server.model

import jakarta.persistence.*

@Entity
@Table
class Product(
    var id: Long,
    var serialNumber:Long,
    var deviceType:String,
    var model:String,


    @ManyToOne(cascade = [CascadeType.MERGE]) var owner:Customer,


):EntityBase<Long>() {

}


/*fun ProductDTO.toModel(owner: Customer): Product{
    return Product(deviceType, model, serialNumber, owner)
}*/