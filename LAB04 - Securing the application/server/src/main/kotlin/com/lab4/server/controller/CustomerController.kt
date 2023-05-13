package com.lab4.server.controller


import com.lab4.server.dto.CustomerDTO
import com.lab4.server.dto.CustomerFormModification
import com.lab4.server.dto.CustomerFormRegistration
import com.lab4.server.exception.Exception
import com.lab4.server.service.CustomerServiceImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerController @Autowired constructor(val profileService: CustomerServiceImpl) {


    @GetMapping("/api/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerById(@PathVariable("email") email:String): CustomerDTO?{
        var profile: CustomerDTO? = profileService.getProfileByEmail(email)
            ?: throw Exception.ProfileNotFoundException("This profile couldn't be found")

        return profile
    }


    @PostMapping("/api/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProfile(@RequestBody @Valid profile:CustomerFormRegistration, br:BindingResult){
        if(br.hasErrors()){
            val invalidFields = br.fieldErrors.map { it.field }
            throw Exception.ValidationException("", invalidFields)
        }
        else if(profileService.getProfileByEmail(profile.email) != null) {
            throw Exception.ProfileAlreadyExistingException("A profile with this email already exists")
        }

        profileService.addProfile(profile)
    }


    /**
     * Controller function used to manage updated of the user's profile. The new profile is
     * validated against the CustomerFormModification, and it is passed to the service in order
     * to update the database.
     *
     * @param email the email of the user whose profile needs to be updated.
     * @param profile the updated profile of the user
     */
    @PutMapping("/api/profiles/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editProfile(
        @PathVariable("email") email: String,
        @RequestBody @Valid profile: CustomerFormModification,
        br: BindingResult
    ) {

        /* Checking errors */
        if (br.hasErrors()) {
            val invalidFields = br.fieldErrors.map { it.field }
            throw Exception.ValidationException("", invalidFields)
        } else if (profileService.getProfileByEmail(email) == null) {
            throw Exception.ProfileNotFoundException("This profile couldn't be found")
        }

        /* Updating... */
        profileService.editProfile(email, profile)

    }


}