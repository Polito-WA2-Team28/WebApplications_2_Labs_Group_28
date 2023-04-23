package com.lab3.server.repository

import com.lab3.server.model.Profile
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository : JpaRepository<Profile, Int> {
    fun findByEmail(email: String): Profile?
}