package com.final_project.server.service

import com.final_project.server.model.Manager
import java.util.UUID

interface ManagerService {

    fun getManagerById(id: UUID): Manager?
}