package com.lab2.server.service

import com.lab2.server.dto.ProductDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.model.Product
import org.springframework.stereotype.Service
import com.lab2.server.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.util.UUID

@Service
class ProductServiceImpl @Autowired constructor(private val productRepository: ProductRepository) : ProductService {

    override fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll().map{it -> it.toDTO()}
    }

    override fun getProductById(id: Int): ProductDTO? {
        return productRepository.findByIdOrNull(id)?.toDTO()
    }
}