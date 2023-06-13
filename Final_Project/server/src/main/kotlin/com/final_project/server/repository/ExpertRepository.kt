package com.final_project.server.repository

import com.final_project.server.model.Expert
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExpertRepository: JpaRepository<Expert, UUID> {
}