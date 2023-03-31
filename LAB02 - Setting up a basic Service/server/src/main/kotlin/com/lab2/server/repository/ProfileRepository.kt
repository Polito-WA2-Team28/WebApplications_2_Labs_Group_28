package com.lab2.server.repository

import com.lab2.server.model.Customer
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ProfileRepository : CrudRepository<Customer, UUID> {
}