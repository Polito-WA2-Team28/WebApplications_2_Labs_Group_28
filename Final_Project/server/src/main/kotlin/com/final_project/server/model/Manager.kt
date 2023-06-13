package com.final_project.server.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table
class Manager(
    @Id var id:UUID,
    var email:String
)