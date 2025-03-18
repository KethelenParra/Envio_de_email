package infrastructure.shared;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@ApplicationScoped
public class BaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(BaseRepository.class.getName());

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @SuppressWarnings("unchecked")
    protected <T> T castUtils(final Object object) {
        return (T) object;
    }

    protected void configureNativeQueryWithDto(
            final Query query,
            final Class<?> classe) {

        final NativeQuery<?> nativeQuery = query.unwrap(NativeQuery.class);
        nativeQuery.setResultTransformer(Transformers.aliasToBean(classe));
    }

    protected void configureNativeQueryWithDto(
            final List<String> campos,
            final Query query,
            final Class<?> classe) {

        final NativeQuery<?> nativeQuery = query.unwrap(NativeQuery.class);
        nativeQuery.setResultTransformer(Transformers.aliasToBean(classe));

        for (final String campo : campos) {
            Field atributo = null;
            Class<?> classeAtual = classe;

            do {
                try {
                    atributo = classeAtual.getDeclaredField(campo.trim());

                } catch (final NoSuchFieldException e) {
                }

            } while ((classeAtual = classeAtual.getSuperclass()) != null);

            if (atributo == null) {
                LOGGER.log(Level.WARNING,
                        String.format("Campo [%s] não foi localizado na classe %s", campo, classe.getName()));
                continue;
            }

            if (atributo.getType() == BigInteger.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.BIG_INTEGER);

            } else if (atributo.getType() == Integer.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.INTEGER);

            } else if (atributo.getType() == BigDecimal.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.BIG_DECIMAL);

            } else if (atributo.getType() == Float.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.FLOAT);

            } else if (atributo.getType() == Double.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.DOUBLE);

            } else if (atributo.getType() == String.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.STRING);

            } else if (atributo.getType() == Character.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.CHARACTER);

            } else if (atributo.getType() == Boolean.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.BOOLEAN);

            } else if (atributo.getType() == Long.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.LONG);

            } else if (atributo.getType() == LocalDate.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.LOCAL_DATE);

                // Usado para converter valores do tipo LocalDateTime
                // Sempre que for necessário retornar um LocalDateTime via DTO utilize o tipo de
                // dado timestamp
            } else if (atributo.getType() == Timestamp.class) {
                nativeQuery.addScalar(campo, StandardBasicTypes.TIMESTAMP);

            } else {
                nativeQuery.addScalar(campo);
            }
        }
    }
}
