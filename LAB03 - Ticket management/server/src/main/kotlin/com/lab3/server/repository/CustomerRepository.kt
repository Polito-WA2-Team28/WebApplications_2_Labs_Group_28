package com.lab3.server.repository

import com.lab3.server.model.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Int> {
    fun findByEmail(email: String): Customer?
}