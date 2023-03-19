package edu.ou.buildingcommandservice.repository.area;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.AreaEntity;
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
public class AreaFindBySlugWithDeletedRepository extends BaseRepository<String, AreaEntity> {
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
     * Find area by slug with deleted
     *
     * @param areaSlug area slug
     * @return area
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected AreaEntity doExecute(String areaSlug) {
        final String hqlQuery = "FROM AreaEntity A WHERE A.slug = :areaSlug";

        try {
            return (AreaEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "areaSlug",
                                    areaSlug
                            )
                            .getSingleResult();

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
     * @param input input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
