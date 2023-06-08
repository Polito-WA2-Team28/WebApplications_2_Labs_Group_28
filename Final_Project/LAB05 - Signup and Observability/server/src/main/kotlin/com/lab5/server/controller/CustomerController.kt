package com.lab5.server.controller

import com.lab5.security.config.SecurityConfig
import com.lab5.server.dto.*
import com.lab5.server.exception.Exception
import com.lab5.server.service.CustomerServiceImpl
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
class CustomerController @Autowired constructor(val profileService: CustomerServiceImpl,
                                                val securityConfig: SecurityConfig) {

    val logger: Logger = LoggerFactory.getLogger(CustomerController::class.java)

    @GetMapping("/api/customers/getProfile")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerById(): CustomerDTO? {
        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())
        val profile = profileService.getCustomerById(customerId)

        if (profile != null)
            return profile.toDTO()

        else {
            logger.error("Endpoint: /api/customers/getProfile\nError: This profile couldn't be found")
            throw Exception.ProfileNotFoundException("This profile couldn't be found")
        }

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
    ) {

        val customerId = UUID.fromString(securityConfig.retrieveUserClaim())

        /* Checking errors */
        if (br.hasErrors()) {
            val invalidFields = br.fieldErrors.map { it.field }
            logger.error("Endpoint: /api/customers/editProfile\nError: Invalid fields: $invalidFields")
            throw Exception.ValidationException("", invalidFields)
        }
        else if (profileService.getCustomerById(customerId) == null) {
            logger.error("Endpoint: /api/customers/editProfile\nError: This profile couldn't be found")
            throw Exception.ProfileNotFoundException("This profile couldn't be found")
        }

        /* Updating... */
        profileService.editProfile(customerId, profile)

    }


}