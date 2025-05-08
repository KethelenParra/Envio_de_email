package infrastructure.auth.dto;

import org.keycloak.representations.AccessTokenResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponseDTO(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("refresh_expires_in") Long refreshExpiresIn) {

    public static TokenResponseDTO from(AccessTokenResponse atr) {
        return new TokenResponseDTO(
                atr.getToken(),
                atr.getRefreshToken(),
                atr.getTokenType(),
                atr.getExpiresIn(),
                atr.getRefreshExpiresIn());
    }
}