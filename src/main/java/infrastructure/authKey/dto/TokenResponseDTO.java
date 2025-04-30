package infrastructure.authKey.dto;

import org.keycloak.representations.AccessTokenResponse;

public record TokenResponseDTO(
        String access_token,
        String token_type,
        Long expires_in,
        Long refresh_expires_in,
        String refresh_token) {
    public static TokenResponseDTO from(AccessTokenResponse atr) {
        return new TokenResponseDTO(
                atr.getToken(),
                atr.getTokenType(),
                atr.getExpiresIn(),
                atr.getRefreshExpiresIn(),
                atr.getRefreshToken());
    }
}
