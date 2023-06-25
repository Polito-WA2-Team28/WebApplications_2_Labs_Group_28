package com.final_project.server.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.server.dto.*
import com.final_project.server.exception.Exception
import com.final_project.server.service.CustomerServiceImpl
import com.final_project.ticketing.util.Nexus
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Observed
class CustomerController @Autowired constructor(
    val customerService: CustomerServiceImpl,
    val securityConfig: SecurityConfig
) {

    val logger: Logger = LoggerFactory.getLogger(CustomerController::class.java)

    @GetMapping("/api/customers/getProfile")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerById(): CustomerDTO? {

        val nexus: Nexus = Nexus(customerService)

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/customers/getProfile")
            .assertCustomerExists(customerId)

        return nexus.customer
    }


    /**
     * Controller function used to manage updated of the user's profile. The new profile is
     * validated against the CustomerFormModification, and it is passed to the service in order
     * to update the database.
     *
     * @param email the email of the user whose profile needs to be updated.
     * @param profile the updated profile of the user
     */
    @PatchMapping("/api/customers/editProfile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editProfile(
        @RequestBody @Valid profile: CustomerFormModification,
        br: BindingResult
    ): CustomerDTO? {

        val nexus: Nexus = Nexus(customerService)

        /* Checking errors */
        if (br.hasErrors()) {
            val invalidFields = br.fieldErrors.map { it.field }
            logger.error("Endpoint: /api/customers/editProfile Error: Invalid fields: $invalidFields")
            throw Exception.ValidationException("", invalidFields)
        }

        /* running checks... */
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim(SecurityConfig.ClaimType.SUB))
        nexus
            .setEndpointForLogger("/api/customers/editProfile")
            .assertCustomerExists(customerId)

        /* Updating... */
        return customerService.editProfile(customerId, profile)
    }
}