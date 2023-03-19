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
public class AreaCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate area id
     *
     * @param areaId area id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer areaId) {
        if (validValidation.isInValid(areaId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "area identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check exist status of area
     *
     * @param areaId id of area
     * @return exist status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer areaId) {
        final String hqlQuery = "FROM AreaEntity A WHERE A.id = :areaId AND A.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "areaId",
                            areaId
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
    protected void postExecute(Integer areaId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
