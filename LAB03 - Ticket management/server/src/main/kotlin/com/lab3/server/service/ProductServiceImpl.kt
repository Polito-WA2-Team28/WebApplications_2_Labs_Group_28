package com.lab3.server.service

import com.lab3.server.dto.ProductDTO
import com.lab3.server.dto.toDTO
import org.springframework.stereotype.Service
import com.lab3.server.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull


@Service
class ProductServiceImpl @Autowired constructor(private val productRepository: ProductRepository) : ProductService {

    override fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll().map{it.toDTO()}
    }

    override fun getProductById(id: Int): ProductDTO? {
        return productRepository.findByIdOrNull(id)?.toDTO()
    }
}