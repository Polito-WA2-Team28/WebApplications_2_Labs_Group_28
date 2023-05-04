package com.lab3.server.repository

import com.lab3.server.model.Expert
import org.springframework.data.jpa.repository.JpaRepository

interface ExpertRepository: JpaRepository<Expert, Long> {
}