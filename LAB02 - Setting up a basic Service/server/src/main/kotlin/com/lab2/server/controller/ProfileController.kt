package com.lab2.server.controller

import com.lab2.server.dto.CustomerDTO
import com.lab2.server.model.Customer
import com.lab2.server.service.ProfileServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ProfileController @Autowired constructor(val profileService: ProfileServiceImpl) {


    @GetMapping("/api/profiles")
    fun getAllCustomers(): List<CustomerDTO> {
        return profileService.getAllProfiles()
    }

    @GetMapping("/api/profiles/{uuid}")
    fun getCustomerById(@PathVariable("uuid") uuid:UUID): CustomerDTO?{
        return profileService.getProfileById(uuid)
    }




}