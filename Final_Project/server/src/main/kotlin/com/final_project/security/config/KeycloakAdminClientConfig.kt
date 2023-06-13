package com.final_project.security.config

import com.final_project.server.config.GlobalConfig
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeycloakAdminClientConfig(
    private val keycloakProperties: KeycloakProperties
) {
    @Bean
    fun keycloakAdminClient(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.serverUrl)
            .realm(keycloakProperties.realm)
            .clientId(keycloakProperties.clientId)
            .username(keycloakProperties.username)
            .password(keycloakProperties.password)
            .grantType(OAuth2Constants.PASSWORD)
            .resteasyClient(
                ResteasyClientBuilderImpl()
                    .connectionPoolSize(10)
                    .build()
            )
            .build()
    }
}

@Configuration
@ConfigurationProperties(prefix = "keycloak")
class KeycloakProperties @Autowired constructor(private val globalConfig: GlobalConfig) {


    var serverUrl = "http://"+globalConfig.keycloakURL+":"+globalConfig.keycloakPort+"/"
    var realm = globalConfig.keycloakRealm
    var clientId = globalConfig.keycloakClient
    val username: String = globalConfig.keycloakUsername
    val password: String = globalConfig.keycloakPassword
}

