package com.lab2.server.service

import com.lab2.server.dto.ProductDTO
import com.lab2.server.model.Product
import java.util.*

interface ProductService {
    fun getAllProducts() : List<ProductDTO>

    fun getProductById(uuid:UUID) : ProductDTO?
}