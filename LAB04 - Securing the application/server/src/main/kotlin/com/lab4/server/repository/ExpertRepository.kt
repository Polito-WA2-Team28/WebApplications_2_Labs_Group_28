package com.lab4.server.repository

import com.lab4.server.model.Expert
import org.springframework.data.jpa.repository.JpaRepository

interface ExpertRepository: JpaRepository<Expert, Long> {
}