package com.lab4.server.service

import com.lab4.server.dto.ProductDTO
import com.lab4.server.dto.toDTO
import com.lab4.server.model.Product
import org.springframework.stereotype.Service
import com.lab4.server.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull


@Service
class ProductServiceImpl @Autowired constructor(private val productRepository: ProductRepository) : ProductService {

    override fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll().map{it.toDTO()}
    }

    override fun getProductBySerialNumber(serialNumber: Long): Product? {
        return productRepository.findBySerialNumber(serialNumber)
    }

    override fun getProductById(id: Long): ProductDTO? {
        return productRepository.findByIdOrNull(id)?.toDTO()
    }
}