package edu.ou.buildingcommandservice.repository.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntity;
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
public class OwnerHistoryAddRepository extends BaseRepository<OwnerHistoryEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate owner history entity
     *
     * @param ownerHistoryEntity owner history entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(OwnerHistoryEntity ownerHistoryEntity) {
        if (validValidation.isInValid(ownerHistoryEntity, OwnerHistoryEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new owner history entity
     *
     * @param ownerHistoryEntity input of task
     * @return id of owner history
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(OwnerHistoryEntity ownerHistoryEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(ownerHistoryEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "owner history"
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
