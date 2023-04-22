package com.lab2.server.controller

import com.lab2.server.dto.ProductDTO
import com.lab2.server.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.lab2.server.exception.Exception
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@RestController
class ProductController @Autowired constructor(val productService: ProductServiceImpl){

    @GetMapping("/API/products")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() : List<ProductDTO>{
        return productService.getAllProducts()
    }

    @GetMapping("/API/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: Int): ProductDTO? {
        return productService.getProductById(productId)
            ?: throw Exception.ProductNotFoundException("No product matched the requested Id")
    }








}