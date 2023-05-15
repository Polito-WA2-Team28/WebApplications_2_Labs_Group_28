package com.lab4.security.config

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod

@Configuration
class KeycloakAdminClientConfig(
    private val keycloakProperties: KeycloakProperties
) {
    @Bean
    fun keycloakAdminClient(): Keycloak {
        //val bearerToken = getBearerToken()

        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.serverUrl)
            .realm(keycloakProperties.realm)
            .clientId(keycloakProperties.clientId)
            .username(keycloakProperties.username)
            .password(keycloakProperties.password)
            //.authorization(bearerToken)
            .grantType(OAuth2Constants.PASSWORD)
            .resteasyClient(
                ResteasyClientBuilderImpl()
                    .connectionPoolSize(10)
                    .build()
            )
            .build()
    }

    /*private fun getBearerToken(): String {
        val restTemplate = RestTemplateBuilder().build()

        val headers = HttpHeaders()
        headers.set("Content-Type", "application/x-www-form-urlencoded")

        val requestBody = "grant_type=password&username=${keycloakProperties.username}&password=${keycloakProperties.password}&client_id=ticketing-service-client"
        val request = HttpEntity(requestBody, headers)

        val response = restTemplate.exchange(
            "${keycloakProperties.serverUrl}/realms/${keycloakProperties.realm}/protocol/openid-connect/token",
            HttpMethod.POST,
            request,
            Map::class.java
        )

        return "Bearer ${response.body?.get("access_token")}"
    }*/
}

@Configuration
@ConfigurationProperties(prefix = "keycloak")
class KeycloakProperties {
    var serverUrl = "http://localhost:8888/"
    var realm = "TicketingServiceRealm"
    var clientId = "ticketing-service-client"
    val username: String = "admin"
    val password: String = "password"
}

