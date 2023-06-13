package com.final_project.server.repository

import com.final_project.server.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository : JpaRepository<Customer, UUID> {
    fun findByEmail(email: String): Customer?
}