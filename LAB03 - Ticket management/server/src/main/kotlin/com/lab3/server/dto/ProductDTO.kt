package com.lab3.server.dto


import com.lab3.server.model.Product
import java.util.*

data class ProductDTO(val id:Long?, val deviceType:String, val model:String, val devicePurchaseDate:Date, val owner: Long?,
                      val warrantyDescription:String, val warrantyExpirationDate: Date, val insurancePurchaseDate:Date?,
                      val insuranceExpirationDate:Date?) {
}

fun Product.toDTO(): ProductDTO{
    return ProductDTO(this.getId(), deviceType, model, devicePurchaseDate, owner.getId(), warrantyDescription, warrantyExpirationDate, insurancePurchaseDate, insuranceExpirationDate)
}