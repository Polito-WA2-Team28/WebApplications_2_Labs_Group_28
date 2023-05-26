package com.lab3.server.service

import com.lab3.server.model.Manager

interface ManagerService {

    fun getManagerById(id: Long): Manager?
}