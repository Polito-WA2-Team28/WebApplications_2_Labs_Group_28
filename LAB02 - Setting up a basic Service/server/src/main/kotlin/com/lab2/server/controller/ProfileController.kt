package com.lab2.server.controller

import com.lab2.server.model.Profile
import com.lab2.server.service.ProfileServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController @Autowired constructor(val profileServicesImpl: ProfileServiceImpl) {

    /**
     * Retrieve the profile associated with the given email.
     *
     * @param email the email associated with the profile
     * @return a profile object
     */
    @GetMapping("/API/profiles/{email}")
    fun getProfileByEmail(@PathVariable("email") email: String): Profile? {
    }


    /**
     * Create a new profile.
     *
     * @param profile the new profile to add to the database.
     */
    @PostMapping("/API/profiles")
    fun createProfile(@RequestBody profile: Profile?) {

    }

    /**
     * Update the information related to the profile associated with the given email.
     *
     * @param email the email associated with the profile.
     * @param profile the new profile to be updated.
     */
    @PutMapping("/API/profiles/{email}")
    fun editProfileByEmail(
        @PathVariable("email") email: String,
        @RequestBody profile: Profile?
    ) {

    }

}