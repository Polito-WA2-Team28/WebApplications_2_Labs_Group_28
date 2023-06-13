package com.final_project.server.service

import com.final_project.server.dto.ProductDTO
import com.final_project.server.dto.toDTO
import com.final_project.server.model.Product
import org.springframework.stereotype.Service
import com.final_project.server.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull


@Service
@Transactional
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