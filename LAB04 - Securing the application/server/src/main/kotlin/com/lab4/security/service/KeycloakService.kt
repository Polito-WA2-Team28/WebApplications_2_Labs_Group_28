//package com.lab4.security.service
//
//import com.lab4.security.config.KeycloakProperties
//import com.lab4.server.dto.CustomerDTO
//import com.lab4.server.dto.CustomerFormRegistration
//import org.keycloak.admin.client.Keycloak
//import org.keycloak.representations.idm.CredentialRepresentation
//import org.keycloak.representations.idm.UserRepresentation
//import org.springframework.stereotype.Service
//
//@Service
//class KeycloakService(private val keycloak: Keycloak, private val keycloakProperties: KeycloakProperties) {
//    fun createUser(customer: CustomerFormRegistration) {
//        //get CustomerFormRegistration and insert fields
//        val user = UserRepresentation()
//        user.isEnabled = true
//        user.username = customer.name + customer.surname
//        user.credentials = listOf(
//            CredentialRepresentation().apply {
//                this.isTemporary = false
//                this.type = CredentialRepresentation.PASSWORD
//                this.value = "test"
//            }
//        )
//        keycloak.realm(keycloakProperties.realm).users().create(user)
//    }
//}