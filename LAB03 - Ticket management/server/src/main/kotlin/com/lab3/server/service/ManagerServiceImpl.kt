package com.lab3.server.service

import com.lab3.server.model.Manager
import com.lab3.server.repository.ManagerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ManagerServiceImpl @Autowired constructor(val managerRepository: ManagerRepository): ManagerService  {
    override fun getManagerById(id: Long): Manager? {
        return managerRepository.findByIdOrNull(id)
    }
}