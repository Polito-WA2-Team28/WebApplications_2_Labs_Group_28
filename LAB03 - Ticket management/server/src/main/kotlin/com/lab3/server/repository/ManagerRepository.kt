package com.lab3.server.repository

import com.lab3.server.model.Manager
import org.springframework.data.jpa.repository.JpaRepository

interface ManagerRepository: JpaRepository<Manager, Long> {
}