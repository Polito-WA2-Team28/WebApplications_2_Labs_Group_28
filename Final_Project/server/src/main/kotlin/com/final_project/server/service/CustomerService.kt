package com.final_project.server.service

import com.final_project.server.dto.CustomerCompleteRegistration
import com.final_project.server.dto.CustomerDTO
import com.final_project.server.dto.CustomerFormModification
import com.final_project.server.model.Customer
import java.util.UUID

interface CustomerService {


    fun getProfileByEmail(email:String) : CustomerDTO?

    fun getCustomerById(id: UUID): CustomerDTO?

    fun addProfile(profile:CustomerCompleteRegistration):CustomerDTO?

    /**
     * Edit an existing profile in the database.
     *
     * @param email the email of the user whose profile needs to be updated
     * @param profile the profile modified by the user in the front-end
     * @return possibly, the new profile updated in the database
     */
    fun editProfile(customerId: UUID, profile: CustomerFormModification): CustomerDTO?
}