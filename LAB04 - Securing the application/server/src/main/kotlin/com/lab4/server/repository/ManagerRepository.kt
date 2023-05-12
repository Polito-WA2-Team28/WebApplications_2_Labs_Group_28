package com.lab4.server.repository

import com.lab4.server.model.Manager
import org.springframework.data.jpa.repository.JpaRepository

interface ManagerRepository: JpaRepository<Manager, Long> {
}