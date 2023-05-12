package com.lab4.server.dto


import com.lab4.server.model.Product

data class ProductDTO(val id:Long?, val serialNumber:Long, val deviceType:String,
                      val model:String, val owner: Long?) {
}

fun Product.toDTO(): ProductDTO{
    return ProductDTO(this.getId(), serialNumber, deviceType, model, owner.getId())
}