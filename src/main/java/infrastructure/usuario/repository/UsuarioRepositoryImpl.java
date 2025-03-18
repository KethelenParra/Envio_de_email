package infrastructure.usuario.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import application.exception.FormValidationException;
import application.usuario.mapper.UsuarioEntityMapper;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import infrastructure.shared.BaseRepository;
import infrastructure.usuario.dto.UsuarioResponseDTO;
import infrastructure.usuario.entity.UsuarioEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

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
        return this.mapper.toModel(this.getEntityManager().find(UsuarioEntity.class, id));
    }

    @Override
    public void save(final Usuario usuario) {
        final var usuarioEntity = this.mapper.toEntity(usuario);
        if (usuarioEntity.getId() == null) {

            Long count = (Long) this.getEntityManager()
                    .createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.servidor.cpf = :cpf", Long.class)
                    .getSingleResult();

            if (count > 0) {
                throw new FormValidationException("Já existe um usuário com esse CPF.");
            }

            this.getEntityManager().persist(usuarioEntity);
        } else {
            this.getEntityManager().merge(usuarioEntity);
        }

        usuario.setId(usuarioEntity.getId());
    }

    @Override
    public void deleteById(final Long id) {
        final Usuario usuario = this.findById(id);

        if (usuario != null) {
            this.getEntityManager().remove(this.mapper.toEntity(usuario));
        }
    }

    @Override
    public boolean existsByservidorId(Long idServidor) {
        String query = "SELECT COUNT(u) > 0 FROM Usuario u WHERE u.servidor.id = :idServidor";
        return em.createQuery(query, Boolean.class)
                .setParameter("idServidor", idServidor)
                .getSingleResult();

    }

    @Override
    public Usuario findByUsername(final String username) {
        String query = "SELECT u FROM Usuario u WHERE u.username = :username";
        UsuarioEntity entity = this.getEntityManager()
                .createQuery(query, UsuarioEntity.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return entity != null ? this.mapper.toModel(entity) : null;
    }

    private List<UsuarioResponseDTO> searchUsuarios(
            final Map<String, Object> filter,
            final int currentPage,
            final int pageSize) {

        final Query query = this.getEntityManager().createNativeQuery(this.buildQueryToSearchUsuarios(filter));

        this.configureNativeQueryWithDto(query, UsuarioResponseDTO.class);

        this.setFilterParameters(query, filter);

        return this.castUtils(query.setFirstResult(currentPage * pageSize).setMaxResults(pageSize).getResultList());
    }

    private String buildQueryToSearchUsuarios(final Map<String, Object> filter) {
        final StringBuilder sql = new StringBuilder();

        sql.append(" SELECT u.ativo AS isAtivo,                 ");
        sql.append("        u.perfil AS perfil,                 ");
        sql.append("        u.id AS id,                         ");
        sql.append("        s.nome AS nome,                     ");
        sql.append("        s.cpf AS cpf,                       ");
        sql.append("        s.email AS email                    ");
        sql.append("   FROM Usuario u                           ");
        sql.append("   JOIN Servidor s ON u.idServidor = s.id   ");
        sql.append("  WHERE 1 = 1                               ");

        this.appendFiltersToQuery(sql, filter);

        sql.append(this.appendOrderBy(filter));

        return sql.toString();
    }

    private void appendFiltersToQuery(final StringBuilder sql, final Map<String, Object> filter) {
        if (filter.get("id") != null && !filter.get("id").toString().isEmpty()) {
            sql.append(" AND u.id = :id ");
        }

        if (filter.get("email") != null && !filter.get("email").toString().isEmpty()) {
            sql.append(" AND LOWER(s.email) LIKE LOWER(:email) ");
        }

        if (filter.get("nome") != null && !filter.get("nome").toString().isEmpty()) {
            sql.append(" AND LOWER(s.nome) LIKE LOWER(:nome) ");
        }

        if (filter.get("cpf") != null && !filter.get("cpf").toString().isEmpty()) {
            sql.append(" AND s.cpf = :cpf ");
        }

        if (filter.get("ativo") != null) {
            sql.append(" AND u.ativo = :ativo ");
        }

        if (filter.get("perfil") != null && filter.get("perfil") instanceof List<?>) {
            List<?> perfilList = (List<?>) filter.get("perfil");

            if (!perfilList.isEmpty() && perfilList.get(0) instanceof String) {
                sql.append(" AND u.perfil IN :perfil ");
            }
        }

    }

    private void setFilterParameters(final Query query, final Map<String, Object> filter) {
        if (filter.get("id") != null && !filter.get("id").toString().isEmpty()) {
            query.setParameter("id", filter.get("id"));
        }

        if (filter.get("email") != null && !filter.get("email").toString().isEmpty()) {
            query.setParameter("email", "%" + filter.get("email") + "%");
        }

        if (filter.get("nome") != null && !filter.get("nome").toString().isEmpty()) {
            query.setParameter("nome", "%" + filter.get("nome") + "%");
        }

        if (filter.get("cpf") != null && !filter.get("cpf").toString().isEmpty()) {
            query.setParameter("cpf", filter.get("cpf"));
        }

        if (filter.get("ativo") != null) {
            query.setParameter("ativo", filter.get("ativo"));
        }

        if (filter.get("perfil") != null && filter.get("perfil") instanceof List<?>) {
            List<?> perfilList = (List<?>) filter.get("perfil");

            if (!perfilList.isEmpty() && perfilList.get(0) instanceof String) {
                query.setParameter("perfil", perfilList);
            }
        }

    }

    private String appendOrderBy(final Map<String, Object> filter) {
        final String sortField = (String) filter.get("sortField");
        final Object sortOrderObj = filter.get("sortOrder");

        final String sortOrder = sortOrderObj instanceof Integer
                ? ((Integer) sortOrderObj == 1 ? "ASC" : "DESC")
                : (String) sortOrderObj;

        if (sortField == null || sortField.isEmpty()) {
            return " ORDER BY s.nome ASC";
        }

        final String dbField = switch (sortField) {
            case "nome" -> "s.nome";
            case "cpf" -> "s.cpf";
            case "email" -> "s.email";
            case "perfil" -> "u.perfil";
            default -> null;
        };

        if (dbField == null) {
            return "";
        }

        return " ORDER BY " + dbField + " " + ("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
    }

    private long countUsuarios(final Map<String, Object> filter) {
        final StringBuilder sql = new StringBuilder();

        sql.append(" SELECT COUNT(u.id) ");
        sql.append("   FROM Usuario u ");
        sql.append("   JOIN Servidor s ON u.idServidor = s.id ");
        sql.append("  WHERE 1 = 1 ");

        this.appendFiltersToQuery(sql, filter);

        final Query query = this.getEntityManager().createNativeQuery(sql.toString());

        this.setFilterParameters(query, filter);

        return ((Number) query.getSingleResult()).longValue();
    }

}
