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
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingSpaceDeleteRepository extends BaseRepository<ParkingSpaceEntityPK, ParkingSpaceEntityPK> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate parking space id
     *
     * @param parkingSpaceId parking space id
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
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist parking space
     *
     * @param parkingSpaceId slug of exist parking space
     * @return id of exist parkingSpace
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ParkingSpaceEntityPK doExecute(ParkingSpaceEntityPK parkingSpaceId) {
        final String hqlQuery =
                "DELETE FROM ParkingSpaceEntity PS " +
                        "WHERE PS.parkingId = :parkingId AND " +
                        "PS.parkingTypeId = :parkingTypeId";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "parkingId",
                                    parkingSpaceId.getParkingId()
                            )
                            .setParameter(
                                    "parkingTypeId",
                                    parkingSpaceId.getParkingTypeId()
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return parkingSpaceId;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "parking space",
                    "parking space identity",
                    parkingSpaceId
            );

        }
    }

    /**
     * close connection
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
