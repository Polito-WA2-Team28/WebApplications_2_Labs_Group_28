package com.lab3.server.model

import jakarta.persistence.*

@Entity
@Table
class Manager(
    var email:String
): EntityBase<Long>() {
}