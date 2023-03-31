package com.lab2.server.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

import java.util.*

@Entity
@Table(name="product")
class ProductImpl(var deviceType:String,  var model:String, var devicePurchaseDate:Date, var owner:Profile,
                  var warrantyDescription:String, var warrantyExpirationDate:Date, var insurancePurchaseDate:Date,
                  var insuranceExpirationDate:Date) : Product{


    @Id
    private var serialNumber:UUID = UUID.randomUUID()



// Differences between Warranty and Insurance:

// warranty => you may automatically get a warranty at the moment you buy a product (e.g. 1/2 years)
// insurance => you may buy or not an insurance for your product that adds additional protection to potential device damages
//              (or for when the warranty expires)

// Meaning of dates:
// Warranty has only an expiration date since the beginning of its validity period coincides with the device purchase date
// Insurance has both explicit purchase and an expiration dates


// device pictures?




}