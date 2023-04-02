package com.lab2.server.repository

import com.lab2.server.model.Customer
import org.springframework.data.repository.CrudRepository

interface ProfileRepository : CrudRepository<Customer, Int> {
}