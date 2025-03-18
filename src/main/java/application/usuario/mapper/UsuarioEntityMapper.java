package application.usuario.mapper;

import java.util.stream.Collectors;

import domain.usuario.model.Usuario;
import infrastructure.usuario.entity.PerfilEnum;
import infrastructure.usuario.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioEntityMapper {

    public UsuarioEntity toEntity(final Usuario model) {
        final UsuarioEntity entity = new UsuarioEntity();

        entity.setId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setAtivo(model.isAtivo());
        entity.setPerfil(model.getPerfil() != null ? PerfilEnum.fromDomain(model.getPerfil()) : null);

        return entity;
    }

    public Usuario toModel(final UsuarioEntity entity) {
        final Usuario usuario = new Usuario();

        usuario.setId(entity.getId());
        usuario.setUsername(entity.getUsername());
        usuario.setAtivo(entity.isAtivo());
        usuario.setPerfil(entity.getPerfil() != null ? entity.getPerfil().toDomain() : null);

        return usuario;
    }

}
