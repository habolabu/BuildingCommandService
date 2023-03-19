package edu.ou.buildingcommandservice.repository.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
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
public class RoomAddRepository extends BaseRepository<RoomEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate room entity
     *
     * @param room room
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(RoomEntity room) {
        if (validValidation.isInValid(room, RoomEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new room
     *
     * @param room room
     * @return id of new room
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(RoomEntity room) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(room);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "room"
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
    protected void postExecute(RoomEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
