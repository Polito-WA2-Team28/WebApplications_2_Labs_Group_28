package com.lab2.server.dto


import com.lab2.server.model.Customer
import com.lab2.server.model.Product
import java.util.*

data class ProductDTO(val deviceType:String, val model:String, val devicePurchaseDate:Date, val owner: UUID,
                      val warrantyDescription:String, val warrantyExpirationDate: Date, val insurancePurchaseDate:Date,
                      val insuranceExpirationDate:Date) {
}

fun Product.toDTO(): ProductDTO{
    return ProductDTO(deviceType, model, devicePurchaseDate, ownerId, warrantyDescription, warrantyExpirationDate, insurancePurchaseDate, insuranceExpirationDate)
}