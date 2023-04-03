package com.lab2.server.service

import com.lab2.server.dto.ProfileDTO
import com.lab2.server.dto.toDTO
import com.lab2.server.repository.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileServiceImpl @Autowired constructor(val profileRepository: ProfileRepository) : ProfileService {


    override fun getProfileByEmail(email: String): ProfileDTO? {
        return profileRepository.findByEmail(email)?.toDTO()
    }


    //Validate body
    //Insert
    override fun addProfile(profile: ProfileDTO) {
        TODO("Not yet implemented")
    }

    //Validate body
    //Update
    override fun editProfile(profile: ProfileDTO) {
        TODO("Not yet implemented")
    }
}