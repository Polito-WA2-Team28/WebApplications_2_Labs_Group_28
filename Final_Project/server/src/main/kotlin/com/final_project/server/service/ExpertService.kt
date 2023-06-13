package com.final_project.server.service

import com.final_project.server.dto.ExpertCompleteRegistration
import com.final_project.server.dto.ExpertDTO
import com.final_project.server.model.Expert
import java.util.UUID

interface ExpertService {

    fun getExpertById(id: UUID): Expert?

    fun addProfile(profile: ExpertCompleteRegistration): ExpertDTO?
}