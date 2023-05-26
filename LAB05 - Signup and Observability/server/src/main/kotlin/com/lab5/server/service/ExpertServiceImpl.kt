package com.lab5.server.service

import com.lab5.server.model.Expert
import com.lab5.server.repository.ExpertRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExpertServiceImpl @Autowired constructor(val expertRepository: ExpertRepository): ExpertService  {
    override fun getExpertById(id: UUID): Expert? {
        return expertRepository.findByIdOrNull(id)
    }
}