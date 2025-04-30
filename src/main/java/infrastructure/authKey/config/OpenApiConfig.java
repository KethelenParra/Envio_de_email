package infrastructure.authKey.config;

import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@SecurityScheme(securitySchemeName = "bearer-jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Bearer token JWT")
public class OpenApiConfig {

}
