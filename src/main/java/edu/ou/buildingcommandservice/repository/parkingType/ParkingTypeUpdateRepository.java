package edu.ou.buildingcommandservice.repository.parkingType;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingTypeEntity;
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
public class ParkingTypeUpdateRepository extends BaseRepository<ParkingTypeEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate parking type
     *
     * @param parkingType parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingTypeEntity parkingType) {
        if (validValidation.isInValid(parkingType, ParkingTypeEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist parking type
     *
     * @param parkingType parking type information
     * @return id of updated parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingTypeEntity parkingType) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            ParkingTypeEntity.class,
                            parkingType.getId()
                    )
                    .setName(parkingType.getName())
                    .setSlug(parkingType.getSlug())
                    .setIsDeleted(parkingType.getIsDeleted());
            entityTransaction.commit();

            return parkingType.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "parking type"
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
    protected void postExecute(ParkingTypeEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
