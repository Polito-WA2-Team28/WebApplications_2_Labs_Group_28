package com.lab3.server.dto


import com.lab3.server.model.Product
import java.util.*

data class ProductDTO(val id:Long?, val serialNumber:Long, val deviceType:String,
                      val model:String, val owner: Long?) {
}

fun Product.toDTO(): ProductDTO{
    return ProductDTO(this.getId(), serialNumber, deviceType, model, owner.getId())
}