package edu.ou.buildingcommandservice.repository.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntityPK;
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
public class OwnerHistoryCheckDeleteRepository extends BaseRepository<OwnerHistoryEntityPK, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate owner history id
     *
     * @param ownerHistoryId owner history id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(OwnerHistoryEntityPK ownerHistoryId) {
        if (validValidation.isInValid(ownerHistoryId, OwnerHistoryEntityPK.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check deleted status of owner history
     *
     * @param ownerHistoryId owner history id
     * @return deleted status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(OwnerHistoryEntityPK ownerHistoryId) {
        final String hqlQuery = "FROM OwnerHistoryEntity O " +
                "WHERE O.roomId = :roomId AND O.ownerId = :ownerId AND O.isDeleted IS NOT NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "roomId",
                            ownerHistoryId.getRoomId()
                    )
                    .setParameter(
                            "ownerId",
                            ownerHistoryId.getOwnerId()
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
     * @param input input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(OwnerHistoryEntityPK input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
