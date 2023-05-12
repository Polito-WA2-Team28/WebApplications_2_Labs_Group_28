package com.lab4.security.controller

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class UserController {

    @PostMapping("/api/auth/login")
    fun authenticateUser(/*@RequestBody @Valid userCredentials: UserCredentialsDTO*/){
        println("test")
        val restTemplate:RestTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map: MutableMap<String, String> = mutableMapOf()

        map["grant_type"] = "password"
        map["client_id"] = "ticketing-service-client"
        map["username"] = "customer-test"
        map["password"] = "customer-test"

        val tokenEndpoint = "http://localhost:8888/realms/TicketingServiceRealm/protocol/openid-connect/token"
        val entity = HttpEntity(map, headers)

        val response = restTemplate.postForEntity(tokenEndpoint, entity, String::class.java)

        println(response)
    }
}