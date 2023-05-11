package com.lab3.security.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/experts/**").hasRole("EXPERT")
            .requestMatchers("/api/managers/**").hasRole("MANAGER")
            .anyRequest().authenticated()
            .and().logout().permitAll()
            .and().formLogin()


        http.oauth2ResourceServer().jwt()

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        return http.build()
    }

}