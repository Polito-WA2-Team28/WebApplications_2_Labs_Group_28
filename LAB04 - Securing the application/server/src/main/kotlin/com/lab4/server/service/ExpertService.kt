package com.lab4.server.service

import com.lab4.server.model.Expert
import java.util.UUID

interface ExpertService {

    fun getExpertById(id: UUID): Expert?
}