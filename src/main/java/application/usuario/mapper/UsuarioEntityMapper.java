package application.usuario.mapper;

import domain.usuario.model.Usuario;
import infrastructure.usuario.entity.PerfilEnum;
import infrastructure.usuario.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioEntityMapper {

    public UsuarioEntity toEntity(final Usuario model) {
        final UsuarioEntity entity = new UsuarioEntity();

        entity.setId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setName(model.getName());
        entity.setCpf(model.getCpf());
        entity.setEmail(model.getEmail());
        entity.setSenha(model.getSenha());
        entity.setPerfil(model.getPerfil() != null ? PerfilEnum.fromDomain(model.getPerfil()) : null);
        entity.setKeycloakId(model.getKeycloakId());

        return entity;
    }

    public Usuario toModel(final UsuarioEntity entity) {
        final Usuario usuario = new Usuario();

        usuario.setId(entity.getId());
        usuario.setUsername(entity.getUsername());
        usuario.setName(entity.getName());
        usuario.setCpf(entity.getCpf());
        usuario.setEmail(entity.getEmail());
        usuario.setSenha(entity.getSenha());
        usuario.setPerfil(entity.getPerfil() != null ? entity.getPerfil().toDomain() : null);
        usuario.setKeycloakId(entity.getKeycloakId());

        return usuario;
    }
}
