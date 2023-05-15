package com.lab4.security.service

import com.lab4.security.config.KeycloakProperties
import com.lab4.server.dto.CustomerCompleteRegistration
import com.lab4.server.dto.CustomerFormRegistration
import com.lab4.server.dto.toCompleteCustomer
import com.lab4.server.service.CustomerServiceImpl
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*
import javax.ws.rs.core.Response


@Service
class KeycloakService(private val keycloak: Keycloak,
                      private val keycloakProperties: KeycloakProperties,
                      private val customerService: CustomerServiceImpl) {
    fun createUser(customer: CustomerFormRegistration): Response {
        val role = keycloak.realm(keycloakProperties.realm).roles()["CUSTOMER"].toRepresentation()

        val user = UserRepresentation()
        user.isEnabled = true
        user.isEmailVerified = true
        user.username = customer.name
        user.email = customer.email
        user.credentials = listOf(
            CredentialRepresentation().apply {
                this.isTemporary = false
                this.type = CredentialRepresentation.PASSWORD
                this.value = "test"
                // Don't use hardcoded password, modify DTO to accept password from customer
            }
        )

        val response = keycloak.realm(keycloakProperties.realm).users().create(user)

        if(response.status == Response.Status.CREATED.statusCode){
            //Retrieve newly generated User ID
            val uri: URI = response.location
            val path: String = uri.getPath()
            val segments = path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val userId = segments[segments.size - 1]

            //Assign Customer Role
            keycloak.realm(keycloakProperties.realm).users().get(userId).roles().realmLevel().add(listOf(role))

            //Propagate User creation to Postgres DB
            val completeProfile:CustomerCompleteRegistration = customer.toCompleteCustomer(UUID.fromString(userId), customer)
            customerService.addProfile(completeProfile)
        }

        return response
    }
}