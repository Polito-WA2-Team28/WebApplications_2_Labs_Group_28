package com.lab2.server.controller

import com.lab2.server.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
class ProductController @Autowired constructor(val productService: ProductServiceImpl) {

    @GetMapping("/api/products")
    fun getProducts(){
        productService.getAllProducts()
    }

    @GetMapping("/api/products/{productId}")
    fun getProductById(@PathVariable("productId") productId: UUID){
        productService.getProductById(productId)
    }








}