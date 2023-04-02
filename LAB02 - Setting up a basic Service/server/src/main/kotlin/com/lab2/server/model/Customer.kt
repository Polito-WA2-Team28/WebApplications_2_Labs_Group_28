package com.lab2.server.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name="profile")
class Customer(@Id @Column(name="profile_id") var id:Int, var name:String,
               var surname:String, var registrationDate:Date, var birthDate:Date,
               var email:String, var phoneNumber:String){


}