package com.lab2.server.repository

import com.lab2.server.model.Product
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository : CrudRepository<Product, Int> {
}