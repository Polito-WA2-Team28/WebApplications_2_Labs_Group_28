package com.lab5.server.service

import com.lab5.server.model.Manager
import com.lab5.server.repository.ManagerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ManagerServiceImpl @Autowired constructor(val managerRepository: ManagerRepository): ManagerService  {
    override fun getManagerById(id: UUID): Manager? {
        return managerRepository.findByIdOrNull(id)
    }
}