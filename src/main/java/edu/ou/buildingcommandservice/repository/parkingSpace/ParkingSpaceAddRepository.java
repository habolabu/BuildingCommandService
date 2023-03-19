package edu.ou.buildingcommandservice.repository.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntity;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceAddRepository extends BaseRepository<ParkingSpaceEntity, ParkingSpaceEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking space entity
     *
     * @param parkingSpaceEntity parking space entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingSpaceEntity parkingSpaceEntity) {
        if (validValidation.isInValid(parkingSpaceEntity, ParkingSpaceEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking space"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new parking space entity
     *
     * @param parkingSpaceEntity input of task
     * @return id of parking space
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ParkingSpaceEntityPK doExecute(ParkingSpaceEntity parkingSpaceEntity) {
        try {

            final Session session = entityManager.unwrap(Session.class);
            final Transaction transaction = session.beginTransaction();

            final ParkingSpaceEntityPK parkingSpaceEntityPK = (ParkingSpaceEntityPK) session.save(parkingSpaceEntity);

            transaction.commit();
            session.close();

            return parkingSpaceEntityPK;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
