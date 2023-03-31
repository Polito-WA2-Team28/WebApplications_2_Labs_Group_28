package com.lab2.server.service

import org.springframework.stereotype.Service
import com.lab2.server.model.Product
import com.lab2.server.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

@Service
class ProductServiceImpl @Autowired constructor(private val productRepository: ProductRepository) : ProductService {

    fun getAllProducts(): List<Product> {
        return productRepository.findAll().map{it}
    }

    fun getProductById(uuid: UUID): Product? {
        return productRepository.findByIdOrNull(uuid.toString())
    }
}