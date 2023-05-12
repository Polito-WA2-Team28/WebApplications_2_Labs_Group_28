package com.lab4.server.service

import com.lab4.server.model.Manager

interface ManagerService {

    fun getManagerById(id: Long): Manager?
}