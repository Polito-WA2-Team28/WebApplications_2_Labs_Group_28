package com.final_project.server.service

import com.final_project.server.dto.ProductDTO
import com.final_project.server.model.Product

interface ProductService {
    fun getAllProducts() : List<ProductDTO>

    fun getProductById(id:Long) : ProductDTO?

    fun getProductBySerialNumber(serialNumber:Long): Product?
}