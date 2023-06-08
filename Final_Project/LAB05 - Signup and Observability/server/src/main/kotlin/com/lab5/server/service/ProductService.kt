package com.lab5.server.service

import com.lab5.server.dto.ProductDTO
import com.lab5.server.model.Product

interface ProductService {
    fun getAllProducts() : List<ProductDTO>

    fun getProductById(id:Long) : ProductDTO?

    fun getProductBySerialNumber(serialNumber:Long): Product?
}