package com.lab2.server.controller


import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.ProfileFormModification
import com.lab2.server.dto.ProfileFormRegistration
import com.lab2.server.dto.toDTO
import com.lab2.server.exception.Exception
import com.lab2.server.model.toModel
import com.lab2.server.service.ProfileServiceImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
class ProfileController @Autowired constructor(val profileService: ProfileServiceImpl) {


    @GetMapping("/api/profiles/{email}")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerById(@PathVariable("email") email:String): ProfileDTO?{
        var profile: ProfileDTO? = profileService.getProfileByEmail(email)
            ?: throw Exception.ProfileNotFoundException("This profile couldn't be found")

        return profile
    }


    //Create another class similar to Profile without an ID

    //Validate body => @Valid annotations compares against constraints in ProfileForm class
    //Check if email exists (there should be a repository method that either saves or returns an error) => checked in service
    //Save => works
    @PostMapping("/api/profiles")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProfile(@RequestBody @Valid profile:ProfileFormRegistration, br:BindingResult){
        if(br.hasErrors()){
            //validation error
            println(br.fieldErrors)
            throw Exception.ValidationException("", br.fieldErrors)
        }
        else if(profileService.getProfileByEmail(profile.email) != null) {
            throw Exception.ProfileAlreadyExistingException("A profile with this email already exists")
        }

        profileService.addProfile(profile)
    }


    /**
     * Controller function used to manage updated of the user's profile. The new profile is
     * validated against the ProfileFormModification, and it is passed to the service in order
     * to update the database.
     *
     * @param email the email of the user whose profile needs to be updated.
     * @param profile the updated profile of the user
     */
    @PutMapping("/api/profiles/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editProfile(
        @PathVariable("email") email: String,
        @RequestBody @Valid profile: ProfileFormModification,
        br: BindingResult
    ) {

        /* Checking validation errors */
        if (br.hasErrors()) {
            throw Exception.ValidationException("", br.fieldErrors)
        } else if (profileService.getProfileByEmail(email) == null) {
            throw Exception.ProfileNotFoundException("This profile couldn't be found")
        }

        profileService.editProfile(email, profile)
    }


}