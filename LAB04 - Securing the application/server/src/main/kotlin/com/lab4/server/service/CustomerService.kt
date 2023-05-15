package com.lab4.server.service

import com.lab4.server.dto.CustomerCompleteRegistration
import com.lab4.server.dto.CustomerDTO
import com.lab4.server.dto.CustomerFormModification
import com.lab4.server.dto.CustomerFormRegistration
import com.lab4.server.model.Customer
import java.util.UUID

interface CustomerService {


    fun getProfileByEmail(email:String) : CustomerDTO?

    fun getCustomerById(id: UUID): Customer?

    fun addProfile(profile:CustomerCompleteRegistration):CustomerDTO?

    /**
     * Edit an existing profile in the database.
     *
     * @param email the email of the user whose profile needs to be updated
     * @param profile the profile modified by the user in the front-end
     * @return possibly, the new profile updated in the database
     */
    fun editProfile(email: String, profile: CustomerFormModification): CustomerDTO?
}