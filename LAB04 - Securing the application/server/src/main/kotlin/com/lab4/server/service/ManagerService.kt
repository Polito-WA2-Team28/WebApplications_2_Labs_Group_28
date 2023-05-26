package com.lab4.server.service

import com.lab4.server.model.Manager
import java.util.UUID

interface ManagerService {

    fun getManagerById(id: UUID): Manager?
}