package com.lab3.server.service

import com.lab3.server.dto.ProductDTO

interface ProductService {
    fun getAllProducts() : List<ProductDTO>

    fun getProductById(id:Int) : ProductDTO?
}