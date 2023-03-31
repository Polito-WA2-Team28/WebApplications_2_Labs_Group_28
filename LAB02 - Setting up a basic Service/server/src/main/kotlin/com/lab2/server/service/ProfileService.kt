package com.lab2.server.service

import com.lab2.server.dto.CustomerDTO
import java.util.UUID

interface ProfileService {
    fun getAllProfiles() : List<CustomerDTO>

    fun getProfileById(uuid:UUID) : CustomerDTO?
}