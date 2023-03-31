package com.lab2.server.service

import com.lab2.server.dto.CustomerDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.repository.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileServiceImpl @Autowired constructor(val profileRepository: ProfileRepository) : ProfileService {
    override fun getAllProfiles(): List<CustomerDTO> {
        return profileRepository.findAll().map{it -> it.toDTO()}
    }

    override fun getProfileById(uuid: UUID): CustomerDTO? {
        return profileRepository.findByIdOrNull(uuid)?.toDTO()
    }
}