package com.lab2.server.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Table(name="profile")
@Entity
class Customer(var name:String, var surname:String, var registrationDate:Date, var birthDate:Date, var email:String, var phoneNumber:String){

    @Id
    var uuid: UUID = UUID.randomUUID()
}