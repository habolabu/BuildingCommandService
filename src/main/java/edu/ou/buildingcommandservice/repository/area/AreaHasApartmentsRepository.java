package edu.ou.buildingcommandservice.repository.area;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;


@Repository
@RequiredArgsConstructor
public class AreaHasApartmentsRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate area slug
     *
     * @param areaSlug area slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String areaSlug) {
        if (validValidation.isInValid(areaSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "area slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check apartments in area
     *
     * @param areaSlug area slug
     * @return area has apartments or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String areaSlug) {
        final String hqlQuery = "SELECT AP.id " +
                "FROM AreaEntity A " +
                "JOIN ApartmentEntity AP ON A.id = AP.areaId " +
                "WHERE A.slug = :areaSlug AND A.isDeleted IS NULL AND AP.isDeleted IS NULL";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "areaSlug",
                            areaSlug
                    )
                    .getResultList()
                    .isEmpty();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "area",
                    "area slug",
                    areaSlug
            );
        }
    }

    /**
     * Close connection
     *
     * @param areaSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String areaSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
