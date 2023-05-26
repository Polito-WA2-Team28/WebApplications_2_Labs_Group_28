package com.lab4.server.service

import com.lab4.server.dto.ProductDTO
import com.lab4.server.model.Product

interface ProductService {
    fun getAllProducts() : List<ProductDTO>

    fun getProductById(id:Long) : ProductDTO?

    fun getProductBySerialNumber(serialNumber:Long): Product?
}