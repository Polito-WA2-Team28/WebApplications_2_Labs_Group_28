package com.lab3.server.service

import com.lab3.server.dto.ProductDTO
import com.lab3.server.model.Product

interface ProductService {
    fun getAllProducts() : List<ProductDTO>

    fun getProductById(id:Long) : ProductDTO?

    fun getProductBySerialNumber(serialNumber:Long): Product?
}