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
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Observed
class ExpertProductController @Autowired constructor(val productService: ProductServiceImpl,
                                                     val securityConfig: SecurityConfig){

    val logger: Logger = LoggerFactory.getLogger(CustomerProductController::class.java)

    @GetMapping("/api/experts/products")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() : List<ProductDTO>{
        val expertId = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB)

        return productService.getExpertProducts(UUID.fromString(expertId))
    }

    @GetMapping("/api/experts/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: Long): ProductDTO? {
        val expertId = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB)
        val products = productService.expertGetProductById(UUID.fromString(expertId), productId)

        if (products == null) {
            logger.error("Endpoint: /api/experts/products/$productId Error: No product matched the requested Id")
            throw Exception.ProductNotFoundException("No product matched the requested Id")
        }
        return products
    }
}