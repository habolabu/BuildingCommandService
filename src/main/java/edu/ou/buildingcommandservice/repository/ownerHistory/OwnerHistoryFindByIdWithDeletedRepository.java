package edu.ou.buildingcommandservice.repository.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntity;
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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OwnerHistoryFindByIdWithDeletedRepository
        extends BaseRepository<OwnerHistoryEntityPK, OwnerHistoryEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate owner history
     *
     * @param ownerHistoryEntityPK owner history id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(OwnerHistoryEntityPK ownerHistoryEntityPK) {
        if (validValidation.isInValid(ownerHistoryEntityPK, OwnerHistoryEntityPK.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history reference keys"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find owner history entity by id (contains deleted apartment)
     *
     * @param ownerHistoryEntityPK owner history id
     * @return owner history
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected OwnerHistoryEntity doExecute(OwnerHistoryEntityPK ownerHistoryEntityPK) {
        final String hqlQuery = "FROM OwnerHistoryEntity O " +
                "WHERE O.roomId = :roomId AND O.ownerId = :ownerId";
        try {
            return (OwnerHistoryEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "roomId",
                                    ownerHistoryEntityPK.getRoomId()
                            )
                            .setParameter(
                                    "ownerId",
                                    ownerHistoryEntityPK.getOwnerId()
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "owner history",
                    "owner history reference keys",
                    ownerHistoryEntityPK
            );

        }
    }

    /**
     * Connection close
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
