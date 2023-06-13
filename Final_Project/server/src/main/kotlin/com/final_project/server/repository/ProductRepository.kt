package com.final_project.server.repository

import com.final_project.server.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CrudRepository<Product, Long> {
    fun findBySerialNumber(serialNumber: Long): Product?
}