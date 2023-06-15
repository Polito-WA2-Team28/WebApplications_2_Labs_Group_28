package com.final_project.security.controller

import com.final_project.security.config.SecurityConfig
import com.final_project.security.dto.*
import com.final_project.security.service.KeycloakService
import com.final_project.server.config.GlobalConfig
import com.final_project.server.dto.*
import com.final_project.server.exception.Exception

import com.final_project.security.exception.SecurityException
import io.micrometer.observation.annotation.Observed
import jakarta.validation.Valid
import org.slf4j.*
import org.springframework.http.*
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.io.StringReader
import javax.json.Json
import javax.ws.rs.core.Response

@RestController
@Observed
class UserController(
    private val keycloakService: KeycloakService,
    private val globalConfig: GlobalConfig,
    private val securityConfig: SecurityConfig
) {
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/api/auth/login")
    fun authenticateUser(@RequestBody @Valid userCredentials: UserCredentialsDTO):TokenDTO?{
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body:String = "grant_type=password&client_id=ticketing-service-client&username="+userCredentials.username+"&password="+userCredentials.password

        val tokenEndpoint = "http://"+globalConfig.keycloakURL+":"+globalConfig.keycloakPort+"/realms/"+globalConfig.keycloakRealm+"/protocol/openid-connect/token"
        val entity = HttpEntity(body, headers)

        return try {
            val response = restTemplate.postForEntity(tokenEndpoint, entity, String::class.java)

            val jsonReader = Json.createReader(StringReader(response.body))
            val jsonResponse = jsonReader.readObject()

            TokenDTO(jsonResponse.getString("access_token"))
        } catch (e: HttpClientErrorException.Unauthorized) {
            logger.error("Status: 401 Unauthorized")
            throw  SecurityException.UnauthorizedException("401 Unauthorized")
        } catch (e: HttpClientErrorException) {
            logger.error("Status: ${e.statusCode} Error: ${e.message}")
            throw  SecurityException.UnauthorizedException("Status ${e.statusCode} message: ${e.message}")
        } catch (e: Exception) {
            logger.error(e.message)
            throw  SecurityException.UnknownException(e.message)
        }


    }

    @PostMapping("/api/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody @Valid profile: CustomerFormRegistration, br: BindingResult){
        //add try catch
        if(br.hasErrors()){
            val invalidFields = br.fieldErrors.map { it.field }
            logger.error("Endpoint: /api/auth/register Error: Invalid fields in the registration form: $invalidFields")
            throw Exception.ValidationException("", invalidFields)
        }

        val response = keycloakService.createUser(profile)

        if(response.status != Response.Status.CREATED.statusCode){
            logger.error("Endpoint: /api/auth/register Error: It was not possible to register the customer")
            throw Exception.CouldNotRegisterCustomer("It was not possible to register the customer")
        }

    }


    @PostMapping("/api/auth/createExpert")
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpert(
        @RequestBody @Valid expert: ExpertFormRegistration,
        br: BindingResult
    ) {

        /* checking for invalid fields in the registration form */
        if (br.hasErrors()) {
            val invalidFields = br.fieldErrors.map { it.field }
            logger.error("Endpoint: /api/auth/createExpert Error: Invalid fields in the registration form: $invalidFields")
            throw Exception.ValidationException("", invalidFields)
        }

        /* create the expert */
        val response = keycloakService.createExpert(expert)

        if (response.status != Response.Status.CREATED.statusCode){
            logger.error("Endpoint: /api/auth/createExpert Error: An error occurred while trying to creating an expert.")
            throw Exception.CreateExpertException("An error occurred while trying to creating an expert.")
        }

    }
}