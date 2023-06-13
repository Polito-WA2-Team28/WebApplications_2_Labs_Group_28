
package com.final_project.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@ConfigurationProperties(prefix = "jwt.auth.converter")
@Validated
data class JwtAuthConverterProperties (
    var resourceId: String? = null,
    var principalAttribute: String? = null
)
