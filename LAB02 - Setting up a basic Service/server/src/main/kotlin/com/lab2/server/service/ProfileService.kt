package com.lab2.server.service

import com.lab2.server.dto.ProfileDTO
import java.util.UUID

interface ProfileService {


    fun getProfileByEmail(email:String) : ProfileDTO?

    fun addProfile(profile:ProfileDTO)

    fun editProfile(profile:ProfileDTO)
}