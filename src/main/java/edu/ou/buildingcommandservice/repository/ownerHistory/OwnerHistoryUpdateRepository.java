package edu.ou.buildingcommandservice.repository.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OwnerHistoryUpdateRepository extends BaseRepository<OwnerHistoryEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate ownerHistory
     *
     * @param ownerHistory ownerHistory
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(OwnerHistoryEntity ownerHistory) {
        if (validValidation.isInValid(ownerHistory, OwnerHistoryEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist ownerHistory
     *
     * @param ownerHistory ownerHistory information
     * @return id of updated ownerHistory
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(OwnerHistoryEntity ownerHistory) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            OwnerHistoryEntity.class,
                            ownerHistory.getId()
                    )
                    .setOwnerId(ownerHistory.getOwnerId())
                    .setRoomId(ownerHistory.getRoomId())
                    .setJoinDate(ownerHistory.getJoinDate())
                    .setIsDeleted(ownerHistory.getIsDeleted());
            entityTransaction.commit();

            return ownerHistory.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "area"
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
    protected void postExecute(OwnerHistoryEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
