package com.lab4.server.service

import com.lab4.server.dto.ExpertCompleteRegistration
import com.lab4.server.dto.ExpertDTO
import com.lab4.server.model.Expert
import java.util.UUID

interface ExpertService {

    fun getExpertById(id: UUID): Expert?

    fun addProfile(profile: ExpertCompleteRegistration): ExpertDTO?

}