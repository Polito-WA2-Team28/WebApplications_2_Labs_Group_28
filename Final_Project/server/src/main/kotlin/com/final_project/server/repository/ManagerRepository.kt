package com.final_project.server.repository

import com.final_project.server.model.Manager
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ManagerRepository: JpaRepository<Manager, UUID> {
}