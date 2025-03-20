package application.usuario.service;

import infrastructure.usuario.dto.UsuarioLoginResponseDTO;

public interface JwtService {
    String generateJwt(UsuarioLoginResponseDTO dto);
}