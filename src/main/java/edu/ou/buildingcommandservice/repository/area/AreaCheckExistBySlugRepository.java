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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AreaCheckExistBySlugRepository extends BaseRepository<String, Boolean> {
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
     * Check exist status of area
     *
     * @param areaSlug slug of area
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String areaSlug) {
        final String hqlQuery = "FROM AreaEntity A WHERE A.slug = :areaSlug AND A.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "areaSlug",
                            areaSlug
                    )
                    .getSingleResult();
            return true;

        } catch (NoResultException e) {
            return false;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.SERVER_ERROR,
                    Message.Error.UN_KNOWN
            );

        }
    }

    /**
     * Close connection
     *
     * @param areaId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String areaId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
