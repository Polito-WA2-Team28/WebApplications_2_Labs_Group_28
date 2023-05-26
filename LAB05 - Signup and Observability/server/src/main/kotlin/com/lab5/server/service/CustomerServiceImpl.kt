package com.lab5.server.service

import com.lab5.server.dto.*
import com.lab5.server.model.Customer
import com.lab5.server.model.toModel
import com.lab5.server.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerServiceImpl @Autowired constructor(val customerRepository: CustomerRepository) : CustomerService {


    override fun getProfileByEmail(email: String): CustomerDTO? {
        return customerRepository.findByEmail(email)?.toDTO()
    }

    override fun getCustomerById(id: UUID): Customer? {
        return customerRepository.findByIdOrNull(id)
    }


    override fun addProfile(profile: CustomerCompleteRegistration):CustomerDTO? {
        return customerRepository.save(profile.toModel()).toDTO()
    }

    /**
     * Edit an existing profile in the database.
     *
     * @param email the email of the user whose profile needs to be updated
     * @param profile the profile modified by the user in the front-end
     * @return possibly, the new profile updated in the database. Null otherwise.
     */
    override fun editProfile(
        customerId: UUID,
        profile: CustomerFormModification
    ): CustomerDTO? {

        val originalCustomer: Customer? = customerRepository.findByIdOrNull(customerId)
        val updatedCustomer: Customer = profile.toModel(
            id = originalCustomer?.id ?: return null,
            username = originalCustomer.username,
            registrationDate = originalCustomer.registrationDate,
            email = originalCustomer.email
        )

        /* Storing the result in the database. */
        return customerRepository.save(updatedCustomer).toDTO()
    }
}