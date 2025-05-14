package infrastructure.usuario.repository;

import java.util.List;
import java.util.stream.Collectors;

import application.usuario.mapper.UsuarioEntityMapper;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import infrastructure.shared.BaseRepository;
import infrastructure.usuario.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class UsuarioRepositoryImpl extends BaseRepository implements UsuarioRepository {

    @PersistenceContext
    private EntityManager em;

    private final UsuarioEntityMapper mapper;

    @Inject
    public UsuarioRepositoryImpl(final UsuarioEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Usuario> findAll() {
        final List<UsuarioEntity> entities = this.getEntityManager()
                .createQuery("SELECT u FROM Usuario u ORDER BY u.username", UsuarioEntity.class)
                .getResultList();

        return entities.stream().map(this.mapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Usuario findById(final Long id) {
        UsuarioEntity entity = this.getEntityManager().find(UsuarioEntity.class, id);
        return entity != null ? this.mapper.toModel(entity) : null;
    }

    @Override
    public void save(final Usuario usuario) {
        final var usuarioEntity = this.mapper.toEntity(usuario);
        if (usuarioEntity.getId() == null) {
            this.getEntityManager().persist(usuarioEntity);
        } else {
            this.getEntityManager().merge(usuarioEntity);
        }
        usuario.setId(usuarioEntity.getId());
    }

    @Override
    public void deleteById(final Long id) {
        UsuarioEntity entity = this.getEntityManager().find(UsuarioEntity.class, id);
        if (entity != null) {
            this.getEntityManager().remove(entity);
        }
    }

    @Override
    public Usuario findByUsername(String username) {
        String query = "SELECT u FROM Usuario u WHERE u.username = :username";
        UsuarioEntity entity = this.getEntityManager()
                .createQuery(query, UsuarioEntity.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return entity != null ? this.mapper.toModel(entity) : null;
    }

    @Override
    public Usuario findByEmail(String email) {
        String query = "SELECT u FROM Usuario u WHERE u.email = :email";
        UsuarioEntity entity = this.getEntityManager()
                .createQuery(query, UsuarioEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return entity != null ? this.mapper.toModel(entity) : null;
    }

    @Override
    public Usuario findByCpf(String cpf) {
        String query = "SELECT u FROM Usuario u WHERE u.cpf = :cpf";
        UsuarioEntity entity = this.getEntityManager()
                .createQuery(query, UsuarioEntity.class)
                .setParameter("cpf", cpf)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return entity != null ? this.mapper.toModel(entity) : null;
    }

    @Override
    public Usuario findByUsernameAndSenha(String username, String senha) {
        String query = "SELECT u FROM Usuario u WHERE u.username = :username AND u.senha = :senha";
        UsuarioEntity entity = this.getEntityManager()
                .createQuery(query, UsuarioEntity.class)
                .setParameter("username", username)
                .setParameter("senha", senha)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return entity != null ? this.mapper.toModel(entity) : null;
    }
}
