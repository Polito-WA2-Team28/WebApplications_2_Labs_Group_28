package com.lab5.server.repository

import com.lab5.server.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CrudRepository<Product, Long> {
    fun findBySerialNumber(serialNumber: Long): Product?
}