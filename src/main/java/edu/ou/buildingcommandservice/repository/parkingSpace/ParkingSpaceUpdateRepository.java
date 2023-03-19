package edu.ou.buildingcommandservice.repository.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntity;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
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
public class ParkingSpaceUpdateRepository extends BaseRepository<ParkingSpaceEntity, ParkingSpaceEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate parking space
     *
     * @param parkingSpace parking space
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingSpaceEntity parkingSpace) {
        if (validValidation.isInValid(parkingSpace, ParkingSpaceEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking space"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist parkingSpace
     *
     * @param parkingSpace parkingSpace information
     * @return id of updated parkingSpace
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ParkingSpaceEntityPK doExecute(ParkingSpaceEntity parkingSpace) {
        final ParkingSpaceEntityPK parkingSpaceEntityPK =
                new ParkingSpaceEntityPK()
                        .setParkingId(parkingSpace.getParkingId())
                        .setParkingTypeId(parkingSpace.getParkingTypeId());

        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            ParkingSpaceEntity.class,
                            parkingSpaceEntityPK
                    )
                    .setParkingId(parkingSpaceEntityPK.getParkingId())
                    .setParkingTypeId(parkingSpaceEntityPK.getParkingTypeId())
                    .setCapacity(parkingSpace.getCapacity())
                    .setAvailableSpace(parkingSpace.getAvailableSpace());
            entityTransaction.commit();

            return parkingSpaceEntityPK;

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "parking space"
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
    protected void postExecute(ParkingSpaceEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
