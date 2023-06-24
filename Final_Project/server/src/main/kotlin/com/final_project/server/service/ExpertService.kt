package com.final_project.server.service

import com.final_project.server.dto.ExpertCompleteRegistration
import com.final_project.server.dto.ExpertDTO
import com.final_project.server.model.Expert
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ExpertService {

    fun getAllExpertsWithPaging(pageable: Pageable): Page<ExpertDTO>

    fun getExpertById(id: UUID): ExpertDTO?

    fun addProfile(profile: ExpertCompleteRegistration): ExpertDTO?

}