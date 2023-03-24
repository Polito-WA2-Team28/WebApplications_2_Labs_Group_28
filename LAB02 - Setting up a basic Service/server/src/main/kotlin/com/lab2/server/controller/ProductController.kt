package com.lab2.server.controller

import com.lab2.server.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ProductController @Autowired constructor(private val productService: ProductService) {

    @GetMapping("/api/products")
    fun getProducts(){

    }


}