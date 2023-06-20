package com.final_project.server.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class GlobalConfig {
    @Value("\${keycloak.url}")
    lateinit var keycloakURL:String

    @Value("\${keycloak.port}")
    lateinit var keycloakPort:String

    @Value("\${postgres.url}")
    lateinit var postgresURL:String

    @Value("\${postgres.port}")
    lateinit var postgresPort:String

    @Value("\${keycloak.realm}")
    lateinit var keycloakRealm:String

    @Value("\${keycloak.client}")
    lateinit var keycloakClient:String

    @Value("\${keycloak.username}")
    lateinit var keycloakUsername:String

    @Value("\${keycloak.password}")
    lateinit var keycloakPassword:String

    @Value("\${attachment.directory}")
    lateinit var attachmentsDirectory: String
}