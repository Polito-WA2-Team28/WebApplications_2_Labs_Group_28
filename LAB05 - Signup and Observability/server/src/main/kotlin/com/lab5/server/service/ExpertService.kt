package com.lab5.server.service

import com.lab5.server.model.Expert
import java.util.UUID

interface ExpertService {

    fun getExpertById(id: UUID): Expert?
}