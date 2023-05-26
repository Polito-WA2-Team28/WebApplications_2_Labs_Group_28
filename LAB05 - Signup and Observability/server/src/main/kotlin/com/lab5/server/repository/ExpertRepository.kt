package com.lab5.server.repository

import com.lab5.server.model.Expert
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExpertRepository: JpaRepository<Expert, UUID> {
}