package com.lab3.server.service

import com.lab3.server.dto.ProfileDTO
import com.lab3.server.dto.ProfileFormModification
import com.lab3.server.dto.ProfileFormRegistration
import com.lab3.server.dto.toDTO
import com.lab3.server.model.Customer
import com.lab3.server.model.toModel
import com.lab3.server.repository.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
        val originalCustomer: Customer? = profileRepository.findByEmail(email)
        val updatedCustomer: Customer = profile.toModel(
            id = originalCustomer?.getId() ?: return null,
            registrationDate = originalCustomer.registrationDate,
            email = originalCustomer.email
        )

        /* Storing the result in the database. */
        return profileRepository.save(updatedCustomer).toDTO()
    }
}