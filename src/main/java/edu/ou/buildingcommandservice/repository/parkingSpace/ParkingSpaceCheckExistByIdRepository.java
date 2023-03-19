package edu.ou.buildingcommandservice.repository.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceCheckExistByIdRepository extends BaseRepository<ParkingSpaceEntityPK, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking space id
     *
     * @param parkingSpaceId id of parking space
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingSpaceEntityPK parkingSpaceId) {
        if (validValidation.isInValid(parkingSpaceId, ParkingSpaceEntityPK.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking space identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check parking space exist or not by id
     *
     * @param parkingSpaceId id of parking space
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(ParkingSpaceEntityPK parkingSpaceId) {
        final String hqlQuery = "FROM ParkingSpaceEntity PS " +
                "WHERE PS.parkingId = :parkingId AND PS.parkingTypeId = :parkingTypeId";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "parkingId",
                            parkingSpaceId.getParkingId()
                    ).setParameter(
                            "parkingTypeId",
                            parkingSpaceId.getParkingTypeId()
                    )
                    .getSingleResult();
            return true;

        } catch (NoResultException e) {
            return false;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.SERVER_ERROR,
                    Message.Error.UN_KNOWN
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
    protected void postExecute(ParkingSpaceEntityPK input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
