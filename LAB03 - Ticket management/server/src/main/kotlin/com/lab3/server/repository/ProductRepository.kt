package com.lab3.server.repository

import com.lab3.server.model.Customer
import com.lab3.server.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository : CrudRepository<Product, Long> {
    fun findBySerialNumber(serialNumber: Long): Product?
}