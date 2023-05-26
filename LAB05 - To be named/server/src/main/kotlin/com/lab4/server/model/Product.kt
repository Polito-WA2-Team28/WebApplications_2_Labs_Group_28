package com.lab4.server.model

import jakarta.persistence.*

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