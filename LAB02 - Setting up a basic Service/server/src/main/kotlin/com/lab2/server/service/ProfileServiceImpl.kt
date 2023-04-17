package com.lab2.server.service

import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.ProfileFormModification
import com.lab2.server.dto.ProfileFormRegistration
import com.lab2.server.dto.toDTO
import com.lab2.server.model.Profile
import com.lab2.server.model.toModel
import com.lab2.server.repository.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileServiceImpl @Autowired constructor(val profileRepository: ProfileRepository) : ProfileService {


    override fun getProfileByEmail(email: String): ProfileDTO? {
        return profileRepository.findByEmail(email)?.toDTO()
    }


    override fun addProfile(profile: ProfileFormRegistration):ProfileDTO? {
        return profileRepository.save(profile.toModel()).toDTO()
    }

    /**
     * Edit an existing profile in the database.
     *
     * @param email the email of the user whose profile needs to be updated
     * @param profile the profile modified by the user in the front-end
     * @return possibly, the new profile updated in the database. Null otherwise.
     */
    override fun editProfile(
        email: String,
        profile: ProfileFormModification
    ): ProfileDTO? {

        /* Note: originalProfile must be non-null because email is checked in the controller. */
        val originalProfile: Profile? = profileRepository.findByEmail(email)
        val updatedProfile: Profile = profile.toModel(
            id = originalProfile?.id ?: return null,
            registrationDate = originalProfile.registrationDate,
            email = originalProfile.email
        )

        /* Storing the result in the database. */
        return profileRepository.save(updatedProfile).toDTO()
    }
}