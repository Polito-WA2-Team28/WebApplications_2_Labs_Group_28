package com.lab2.server.controller

import com.lab2.server.dto.ProductDTO
import com.lab2.server.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import com.lab2.server.exception.Exception
import java.util.UUID


@RestController
class ProductController @Autowired constructor(val productService: ProductServiceImpl){

    @GetMapping("/api/products")
    fun getProducts() : List<ProductDTO>{
        return productService.getAllProducts()
    }

    @GetMapping("/api/products/{productId}")
    fun getProductById(@PathVariable("productId") productId: Int): ProductDTO?{

        //check here for null and return either 200 or 404
        var product = productService.getProductById(productId)
            ?: throw Exception.ProductNotFoundException("No product matched the requested Id")

        return product
    }








}