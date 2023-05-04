package com.lab3.server.model

import jakarta.persistence.*

import java.util.*

@Entity
@Table
class Product(
    var deviceType:String,
    var model:String,
    var serialNumber:Long,

    @ManyToOne var owner:Customer,


):EntityBase<Long>() {

}