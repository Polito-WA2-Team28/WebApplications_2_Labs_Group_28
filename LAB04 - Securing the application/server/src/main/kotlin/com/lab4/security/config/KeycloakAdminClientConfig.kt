//package com.lab4.security.config
//
//import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
//import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl
//import org.keycloak.admin.client.Keycloak
//import org.keycloak.admin.client.KeycloakBuilder
//import org.springframework.boot.context.properties.ConfigurationProperties
//import org.springframework.boot.context.properties.EnableConfigurationProperties
//import org.springframework.boot.web.client.RestTemplateBuilder
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//
//@Configuration
//class KeycloakAdminClientConfig(
//    private val keycloakProperties: KeycloakProperties,
//    private val restTemplateBuilder: RestTemplateBuilder
//) {
//    @Bean
//    fun keycloakAdminClient(): Keycloak {
//        val keycloak = KeycloakBuilder.builder()
//            .grantType("password")
//            .serverUrl(keycloakProperties.serverUrl)
//            .realm(keycloakProperties.realm)
//            .clientId(keycloakProperties.clientId)
//            .username(keycloakProperties.username)
//            .password(keycloakProperties.password)
//            .resteasyClient(
//                ResteasyClientBuilderImpl()
//                    .connectionPoolSize(10)
//                    .build()
//            )
//            .build()
//
//        return keycloak
//    }
//}
//
//@Configuration
//@ConfigurationProperties(prefix = "keycloak")
//class KeycloakProperties {
//    var serverUrl = "http://localhost:8888/"
//    var realm = "TicketingServiceRealm"
//    var clientId = "ticketing-service-client"
//    //var clientSecret = "password"
//    val username: String = "admin"
//    val password: String = "password"
//}
//
