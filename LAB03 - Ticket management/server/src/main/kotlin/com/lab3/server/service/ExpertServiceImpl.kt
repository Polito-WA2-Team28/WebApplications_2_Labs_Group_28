package com.lab3.server.service

import com.lab3.server.model.Expert
import com.lab3.server.repository.ExpertRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExpertServiceImpl @Autowired constructor(val expertRepository: ExpertRepository): ExpertService  {
    override fun getExpertById(id: Long): Expert? {
        return expertRepository.findByIdOrNull(id)
    }
}