package com.final_project.server.service

import com.final_project.server.dto.ManagerDTO
import java.util.UUID

interface ManagerService {

    fun getManagerById(id: UUID): ManagerDTO?
}