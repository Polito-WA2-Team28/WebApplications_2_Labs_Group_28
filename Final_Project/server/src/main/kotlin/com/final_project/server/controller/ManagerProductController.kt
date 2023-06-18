package com.final_project.server.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.dto.ProductDTO
import com.final_project.server.exception.Exception
import com.final_project.server.service.ProductServiceImpl
import io.micrometer.observation.annotation.Observed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Observed
class ManagerProductController @Autowired constructor(val productService: ProductServiceImpl){

    val logger: Logger = LoggerFactory.getLogger(CustomerProductController::class.java)

    @GetMapping("/api/managers/products")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() : List<ProductDTO>{
        return productService.getManagerProducts()
    }

    @GetMapping("/api/managers/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: Long): ProductDTO? {
        val products = productService.managerGetProductById(productId)

        if (products == null) {
            logger.error("Endpoint: /api/managers/products/$productId Error: No product matched the requested Id")
            throw Exception.ProductNotFoundException("No product matched the requested Id")
        }
        return products
    }
}