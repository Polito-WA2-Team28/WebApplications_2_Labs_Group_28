package com.lab2.server.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name="profile")
class Customer(var name:String, var surname:String, var registrationDate:Date, var birthDate:Date, var email:String, var phoneNumber:String){

    @Id
    @Column(name="profile_id")
    var uuid: UUID = UUID.randomUUID()
}