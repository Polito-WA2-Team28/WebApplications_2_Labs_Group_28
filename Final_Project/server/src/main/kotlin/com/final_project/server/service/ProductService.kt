package com.final_project.server.service

import com.final_project.server.dto.ProductDTO
import com.final_project.server.model.Product
import java.util.UUID

interface ProductService {
    fun getManagerProducts() : List<ProductDTO>

    fun getCustomerProducts(customerId:UUID) : List<ProductDTO>

    fun getExpertProducts(expertId:UUID) : List<ProductDTO>

    fun managerGetProductById(productId:Long) : ProductDTO?

    fun expertGetProductById(expertId:UUID, productId:Long) : ProductDTO?

    fun customerGetProductById(customerId: UUID, productId: Long): ProductDTO?

    fun customerGetProductBySerialNumber(customerId:UUID, serialNumber:UUID): ProductDTO?

    fun registerProduct(customerId:UUID, productId:Long, serialNumber: UUID)
}