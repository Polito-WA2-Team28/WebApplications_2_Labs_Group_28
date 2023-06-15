package com.lab5.server.repository

import com.lab5.server.model.Manager
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ManagerRepository: JpaRepository<Manager, UUID> {
}