package com.lab2.server.model

import java.util.*

abstract class AbstractProfile(var name:String, var surname:String, var registrationDate:Date,
                               var birthDate: Date, var email:String, var phoneNumber:String):Profile {

    var ID:UUID = UUID.randomUUID()



}