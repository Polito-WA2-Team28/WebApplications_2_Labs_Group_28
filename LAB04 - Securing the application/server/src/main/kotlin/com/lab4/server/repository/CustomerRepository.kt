package com.lab4.server.repository

import com.lab4.server.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerRepository : JpaRepository<Customer, UUID> {
    fun findByEmail(email: String): Customer?
}