package com.final_project.server.service

import com.final_project.server.dto.ExpertCompleteRegistration
import com.final_project.server.dto.ExpertDTO
import com.final_project.server.dto.toDTO
import com.final_project.server.model.Expert
import com.final_project.server.model.toModel
import com.final_project.server.repository.ExpertRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class ExpertServiceImpl @Autowired constructor(val expertRepository: ExpertRepository): ExpertService  {
    override fun getExpertById(id: UUID): Expert? {
        return expertRepository.findByIdOrNull(id)
    }

    override fun addProfile(profile: ExpertCompleteRegistration): ExpertDTO? {
        return this.expertRepository.save(profile.toModel()).toDTO()
    }
}