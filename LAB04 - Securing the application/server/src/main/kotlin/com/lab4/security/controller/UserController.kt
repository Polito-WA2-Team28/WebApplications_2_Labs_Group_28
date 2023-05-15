package com.lab4.security.controller

import com.lab4.security.dto.TokenDTO
import com.lab4.security.dto.UserCredentialsDTO
import com.lab4.security.service.KeycloakService
import com.lab4.server.dto.CustomerFormRegistration
import com.lab4.server.exception.Exception
import jakarta.validation.Valid
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.io.StringReader
import javax.json.Json
import javax.ws.rs.core.Response

@RestController
class UserController(private val keycloakService: KeycloakService) {

    @PostMapping("/api/auth/login")
    fun authenticateUser(@RequestBody @Valid userCredentials: UserCredentialsDTO):TokenDTO?{
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body:String = "grant_type=password&client_id=ticketing-service-client&username="+userCredentials.username+"&password="+userCredentials.password


        val tokenEndpoint = "http://localhost:8888/realms/TicketingServiceRealm/protocol/openid-connect/token"
        val entity = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(tokenEndpoint, entity, String::class.java)

        val jsonReader = Json.createReader(StringReader(response.body))
        val jsonResponse = jsonReader.readObject()


        return TokenDTO(jsonResponse.getString("access_token"))
    }

    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody @Valid profile: CustomerFormRegistration, br: BindingResult){
        if(br.hasErrors()){
            val invalidFields = br.fieldErrors.map { it.field }
            throw Exception.ValidationException("", invalidFields)
        }

        val response = keycloakService.createUser(profile)

        if(response.status != Response.Status.CREATED.statusCode){
            throw Exception.CouldNotRegisterCustomer("It was not possible to register the customer")
        }

    }
}