package com.final_project.security.config


import org.springframework.context.annotation.Bean
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler


@Configuration
@EnableWebSecurity
class SecurityConfig(val jwtAuthConverter:JwtAuthConverter) {
    private val logger: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/customers/**").hasRole("CUSTOMER")
            .requestMatchers("/api/experts/**").hasRole("EXPERT")
            .requestMatchers("/api/managers/**").hasRole("MANAGER")
            .requestMatchers("/api/auth/createExpert").hasRole("MANAGER")
            .requestMatchers("/api/profiles").permitAll()
            .requestMatchers("/api/auth/register").permitAll()
            .requestMatchers("/actuator/prometheus").permitAll()
            .anyRequest().authenticated()
            .and().logout().permitAll()
            .and().formLogin().disable()


        http.csrf().disable()


        http.oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthConverter)

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler(accessDeniedHandler())



        return http.build()
    }


    fun retrieveUserClaim(claimType: ClaimType): String?{
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        var claim:String? = null

        if (authentication is JwtAuthenticationToken) {
            val jwt: Jwt = authentication.token

            when (claimType) {
                ClaimType.SUB -> {
                    val subObject: Any? = jwt.claims["sub"]

                    if (subObject != null) {
                        claim = subObject.toString()
                    }
                }

                ClaimType.USERNAME -> {
                    val subObject: Any? = jwt.claims["preferred_username"]

                    if (subObject != null) {
                        claim = subObject.toString()
                    }
                }
            }
        }

        return claim
    }

    enum class ClaimType {
        USERNAME,
        SUB
    }

    private fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request, response, authException ->
            if (response.status == HttpServletResponse.SC_UNAUTHORIZED) {
                logger.error("Status: ${response.status} ${authException.message}")
            }

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
    }

    private fun accessDeniedHandler(): AccessDeniedHandler {
        return AccessDeniedHandler { request, response, accessDeniedException ->
            if (response.status == HttpServletResponse.SC_FORBIDDEN) {
                logger.error("Status: ${response.status} ${accessDeniedException.message}")
            }

            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
        }
    }


    @Value("\${spring.websecurity.debug:false}")
    var webSecurityDebug = false

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity -> web.debug(webSecurityDebug) }
    }
}