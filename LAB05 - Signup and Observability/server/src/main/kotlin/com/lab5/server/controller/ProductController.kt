package com.lab5.server.controller

import com.lab5.server.dto.ProductDTO
import com.lab5.server.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.lab5.server.exception.Exception
import io.micrometer.observation.annotation.Observed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@RestController
@Observed
class ProductController @Autowired constructor(val productService: ProductServiceImpl){

    val logger: Logger = LoggerFactory.getLogger(ProductController::class.java)

    @GetMapping("/api/products")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() : List<ProductDTO>{
        return productService.getAllProducts()
    }

    @GetMapping("/api/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: Long): ProductDTO? {
        val products = productService.getProductById(productId)
        if (products == null) {
            logger.error("Endpoint: /api/products/$productId Error: No product matched the requested Id")
            throw Exception.ProductNotFoundException("No product matched the requested Id")
        }
        return products
    }
}