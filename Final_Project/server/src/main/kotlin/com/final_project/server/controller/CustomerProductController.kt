package com.final_project.server.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.dto.ProductDTO
import com.final_project.server.service.ProductServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.final_project.server.exception.Exception
import com.final_project.server.dto.RegisterProductDTO
import com.final_project.server.dto.toDTO
import com.final_project.server.model.Product
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@RestController
@Observed
class CustomerProductController @Autowired constructor(val productService: ProductServiceImpl,
                                                       val securityConfig: SecurityConfig){

    val logger: Logger = LoggerFactory.getLogger(CustomerProductController::class.java)

    @GetMapping("/api/customers/products")
    @ResponseStatus(HttpStatus.OK)
    fun getProducts() : List<ProductDTO>{
        val customerId = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB)

        return productService.getCustomerProducts(UUID.fromString(customerId))
    }

    @GetMapping("/api/customers/products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: Long): ProductDTO? {
        val customerId = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB)
        val products = productService.customerGetProductById(UUID.fromString(customerId), productId)

        if (products == null) {
            logger.error("Endpoint: /api/customers/products/$productId Error: No product matched the requested Id")
            throw Exception.ProductNotFoundException("No product matched the requested Id")
        }
        return products
    }

    @PatchMapping("/api/customers/products/registerProduct")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun registerProduct(@RequestBody @Valid productIds: RegisterProductDTO,
                        br: BindingResult
    ){
        if (br.hasErrors()) {
            val invalidFields = br.fieldErrors.map { it.field }
            logger.error("Endpoint: /api/customers/products/registerProduct Error: Invalid fields: $invalidFields")
            throw Exception.ValidationException("", invalidFields)
        }

        val customerId = securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB)

        return productService.registerProduct(UUID.fromString(customerId), productIds.productId, productIds.serialNumber)
    }
}