package edu.ou.buildingcommandservice.repository.parking;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
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
public class ParkingCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking id
     *
     * @param parkingId id of parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer parkingId) {
        if (validValidation.isInValid(parkingId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check parking exist or not by id
     *
     * @param parkingId id of parking
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer parkingId) {
        final String hqlQuery = "FROM ParkingEntity P WHERE P.id = :parkingId AND P.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "parkingId",
                            parkingId
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
     * @param apartmentId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer apartmentId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
