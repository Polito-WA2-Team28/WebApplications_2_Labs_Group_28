package com.lab2.server.service

import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.ProfileForm

interface ProfileService {


    fun getProfileByEmail(email: String?) : ProfileDTO?

    fun addProfile(profile:ProfileForm):ProfileDTO?

    /**
     * Edit an existing profile in the database.
     *
     * @param email the email of the user whose profile needs to be updated
     * @param profile the profile modified by the user in the front-end
     * @return possibly, the new profile updated in the database
     */
    fun editProfile(email: String, profile: ProfileForm): ProfileDTO?
}