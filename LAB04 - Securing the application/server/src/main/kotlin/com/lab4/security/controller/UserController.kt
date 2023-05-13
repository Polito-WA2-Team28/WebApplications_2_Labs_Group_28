package com.lab4.security.controller

import com.lab4.security.dto.TokenDTO
import com.lab4.security.dto.UserCredentialsDTO
import jakarta.validation.Valid
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.io.StringReader
import javax.json.Json

@RestController
class UserController {
    @GetMapping("/api/auth/claims")
    fun showClaims(@AuthenticationPrincipal jwt: Jwt){
        println(jwt.claims)
    }

    @PostMapping("/api/auth/login")
    fun authenticateUser(@RequestBody @Valid userCredentials: UserCredentialsDTO):TokenDTO?{
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map: MutableMap<String, String> = mutableMapOf()
        val body:String = "grant_type=password&client_id=ticketing-service-client&username="+userCredentials.username+"&password="+userCredentials.password


        val tokenEndpoint = "http://localhost:8888/realms/TicketingServiceRealm/protocol/openid-connect/token"
        val entity = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(tokenEndpoint, entity, String::class.java)

        val jsonReader = Json.createReader(StringReader(response.body))
        val jsonResponse = jsonReader.readObject()


        return TokenDTO(jsonResponse.getString("access_token"))
    }
}