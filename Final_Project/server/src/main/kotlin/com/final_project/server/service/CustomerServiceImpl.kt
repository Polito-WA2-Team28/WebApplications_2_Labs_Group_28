package com.final_project.server.service

import com.final_project.server.dto.*
import com.final_project.server.model.Customer
import com.final_project.server.model.toModel
import com.final_project.server.repository.CustomerRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerServiceImpl @Autowired constructor(val customerRepository: CustomerRepository) : CustomerService {

    override fun getProfileByEmail(email: String): CustomerDTO? {
        return customerRepository.findByEmail(email)?.toDTO()
    }

    override fun getCustomerById(id: UUID): CustomerDTO? {
        return customerRepository.findByIdOrNull(id)?.toDTO()
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
            originalCustomer?.id ?: return null,
                originalCustomer.username,
                originalCustomer.registrationDate,
                originalCustomer.email
        )

        /* Storing the result in the database. */
        return customerRepository.save(updatedCustomer).toDTO()
    }
}