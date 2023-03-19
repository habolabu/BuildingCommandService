package edu.ou.buildingcommandservice.repository.parking;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingEntity;
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
public class ParkingAddRepository extends BaseRepository<ParkingEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking entity
     *
     * @param parkingEntity parking entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingEntity parkingEntity) {
        if (validValidation.isInValid(parkingEntity, ParkingEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new parking entity
     *
     * @param parkingEntity input of task
     * @return id of parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingEntity parkingEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(parkingEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "apartment"
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
