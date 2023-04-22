package com.lab2.server.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

import java.util.*

@Entity
@Table(name="product")
class Product(
    @Id var serialNumber:Int,
    var deviceType:String,
    var model:String,
    var devicePurchaseDate:Date,

    @ManyToOne @JoinColumn(name="owner_id") var owner:Profile,
    var warrantyDescription:String,
    var warrantyExpirationDate:Date,
    var insurancePurchaseDate:Date?,
    var insuranceExpirationDate:Date?
) {

}