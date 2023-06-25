package com.final_project.server.service

import com.final_project.server.dto.ExpertCompleteRegistration
import com.final_project.server.dto.ExpertDTO
import com.final_project.server.dto.toDTO
import com.final_project.server.model.Expert
import com.final_project.server.model.toModel
import com.final_project.server.repository.ExpertRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExpertServiceImpl @Autowired constructor(val expertRepository: ExpertRepository): ExpertService  {
    override fun getAllExpertsWithPaging(pageable: Pageable): Page<ExpertDTO> {
        return expertRepository.findAll(pageable)
            .map {
                it.toDTO()
            }
    }

    override fun getExpertById(id: UUID): ExpertDTO? {
        return expertRepository.findByIdOrNull(id)?.toDTO()
    }

    override fun addProfile(profile: ExpertCompleteRegistration): ExpertDTO? {
        return this.expertRepository.save(profile.toModel()).toDTO()
    }
}