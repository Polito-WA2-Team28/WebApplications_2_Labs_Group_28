package com.lab5.server.controller

import com.lab5.server.dto.ProductDTO
import com.lab5.server.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.lab5.server.exception.Exception
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@RestController
class ProductController @Autowired constructor(val productService: ProductServiceImpl){

    @GetMapping("/api/products")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() : List<ProductDTO>{
        return productService.getAllProducts()
    }

    @GetMapping("/api/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: Long): ProductDTO? {
        return productService.getProductById(productId)
            ?: throw Exception.ProductNotFoundException("No product matched the requested Id")
    }
}