package com.lab2.server.repository

import com.lab2.server.model.Profile
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository : JpaRepository<Profile, Int> {
    fun findByEmail(email: String): Profile?
}