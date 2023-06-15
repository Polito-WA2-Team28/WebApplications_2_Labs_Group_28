package com.lab5.server.service

import com.lab5.server.model.Manager
import java.util.UUID

interface ManagerService {

    fun getManagerById(id: UUID): Manager?
}