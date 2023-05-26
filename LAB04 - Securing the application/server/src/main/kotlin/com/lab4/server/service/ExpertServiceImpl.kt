package com.lab4.server.service

import com.lab4.server.dto.ExpertCompleteRegistration
import com.lab4.server.dto.ExpertDTO
import com.lab4.server.dto.toDTO
import com.lab4.server.model.Expert
import com.lab4.server.model.toModel
import com.lab4.server.repository.ExpertRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExpertServiceImpl @Autowired constructor(val expertRepository: ExpertRepository): ExpertService  {
    override fun getExpertById(id: UUID): Expert? {
        return expertRepository.findByIdOrNull(id)
    }

    override fun addProfile(profile: ExpertCompleteRegistration): ExpertDTO? {
        return expertRepository.save(profile.toModel()).toDTO()
    }
}