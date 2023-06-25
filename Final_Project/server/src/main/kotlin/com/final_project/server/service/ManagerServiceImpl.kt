package com.final_project.server.service

import com.final_project.server.dto.ManagerDTO
import com.final_project.server.dto.toDTO
import com.final_project.server.repository.ManagerRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ManagerServiceImpl @Autowired constructor(val managerRepository: ManagerRepository): ManagerService  {
    override fun getManagerById(id: UUID): ManagerDTO? {
        return managerRepository.findByIdOrNull(id)?.toDTO()
    }
}