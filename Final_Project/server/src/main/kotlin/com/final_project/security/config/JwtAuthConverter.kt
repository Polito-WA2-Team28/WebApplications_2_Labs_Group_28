
package com.final_project.security.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

@Component
class JwtAuthConverter(private val properties: JwtAuthConverterProperties) : Converter<Jwt, AbstractAuthenticationToken> {

    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities = (jwtGrantedAuthoritiesConverter.convert(jwt)!!.asSequence() +
                extractResourceRoles(jwt).asSequence()).toSet()
                .map { SimpleGrantedAuthority("ROLE_$it") }
        return JwtAuthenticationToken(jwt, authorities.toList(), getPrincipalClaimName(jwt))
    }

    private fun getPrincipalClaimName(jwt: Jwt): String {
        var claimName = JwtClaimNames.SUB
        if (properties.principalAttribute != null) {
            claimName = properties.principalAttribute!!
        }
        return jwt.getClaim(claimName)
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<String> {
        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access")
        val resource = resourceAccess?.get(properties.resourceId) as? Map<String, Any>
        val resourceRoles = resource?.get("roles") as? Collection<String>

        return resourceRoles ?: emptyList()
    }
}
