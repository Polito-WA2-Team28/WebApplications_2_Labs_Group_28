package com.lab4.server.model

import jakarta.persistence.*

@Entity
@Table
class Manager(
    var email:String
): EntityBase<Long>() {
}