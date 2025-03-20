package infrastructure.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import infrastructure.usuario.dto.UsuarioLoginResponseDTO;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtServiceImpl implements application.usuario.service.JwtService {

    public static final Duration EXPIRATION_TIME = Duration.ofMinutes(15);

    @Override
    public String generateJwt(UsuarioLoginResponseDTO dto) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(EXPIRATION_TIME);

        Set<String> roles = new HashSet<String>();
        roles.add(dto.perfil().name());

        return Jwt.issuer("mail-jwt")
                .subject(dto.username())
                .groups(roles)
                .issuedAt(now)
                .expiresAt(expiryDate)
                .sign();
    }

}