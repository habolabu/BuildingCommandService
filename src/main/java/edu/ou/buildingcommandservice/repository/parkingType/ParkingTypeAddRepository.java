package edu.ou.buildingcommandservice.repository.parkingType;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingTypeEntity;
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
public class ParkingTypeAddRepository extends BaseRepository<ParkingTypeEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking type entity
     *
     * @param parkingTypeEntity parking type entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingTypeEntity parkingTypeEntity) {
        if (validValidation.isInValid(parkingTypeEntity, ParkingTypeEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new parking type entity
     *
     * @param parkingTypeEntity input of task
     * @return id of parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingTypeEntity parkingTypeEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(parkingTypeEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
