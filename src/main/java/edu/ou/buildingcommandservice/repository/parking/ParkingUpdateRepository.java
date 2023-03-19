package edu.ou.buildingcommandservice.repository.parking;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingEntity;
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
public class ParkingUpdateRepository extends BaseRepository<ParkingEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate parking
     *
     * @param parking parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingEntity parking) {
        if (validValidation.isInValid(parking, ParkingEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist parking
     *
     * @param parking parking information
     * @return id of updated parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingEntity parking) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            ParkingEntity.class,
                            parking.getId()
                    )
                    .setName(parking.getName())
                    .setSlug(parking.getSlug())
                    .setApartmentId(parking.getApartmentId())
                    .setIsDeleted(parking.getIsDeleted());
            entityTransaction.commit();

            return parking.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "parking"
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
    protected void postExecute(ParkingEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
