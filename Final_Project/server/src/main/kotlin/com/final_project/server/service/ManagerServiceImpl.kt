package com.final_project.server.service

import com.final_project.server.model.Manager
import com.final_project.server.repository.ManagerRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class ManagerServiceImpl @Autowired constructor(val managerRepository: ManagerRepository): ManagerService  {
    override fun getManagerById(id: UUID): Manager? {
        return managerRepository.findByIdOrNull(id)
    }
}