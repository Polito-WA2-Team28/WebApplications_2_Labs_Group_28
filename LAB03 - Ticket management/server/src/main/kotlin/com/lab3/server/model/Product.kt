package com.lab3.server.model

import jakarta.persistence.*

import java.util.*

@Entity
@Table
class Product(
    var deviceType:String,
    var model:String,

    @Temporal(value= TemporalType.DATE)
    var devicePurchaseDate:Date,

    @ManyToOne var owner:Profile,
    var warrantyDescription:String,

    @Temporal(value= TemporalType.DATE)
    var warrantyExpirationDate:Date,

    @Temporal(value=TemporalType.DATE)
    var insurancePurchaseDate:Date?,

    @Temporal(value=TemporalType.DATE)
    var insuranceExpirationDate:Date?
):EntityBase<Long>() {

}