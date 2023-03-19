package edu.ou.buildingcommandservice.repository.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
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
public class RoomUpdateRepository extends BaseRepository<RoomEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate room
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
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist room
     *
     * @param room input of task
     * @return room id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(RoomEntity room) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            RoomEntity.class,
                            room.getId()
                    )
                    .setName(room.getName())
                    .setSlug(room.getSlug())
                    .setFloorNumber(room.getFloorNumber())
                    .setApartmentId(room.getApartmentId())
                    .setIsDeleted(room.getIsDeleted());
            entityTransaction.commit();

            return room.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
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
