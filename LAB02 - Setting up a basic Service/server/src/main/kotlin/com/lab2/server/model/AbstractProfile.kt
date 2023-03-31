package com.lab2.server.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name="profile")
abstract class AbstractProfile(var name:String, var surname:String, var registrationDate:Date,
                               var birthDate: Date, var email:String, var phoneNumber:String):Profile {
    @Id
    var ID:UUID = UUID.randomUUID()



}