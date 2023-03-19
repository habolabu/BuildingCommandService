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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingHasParkingSpacesRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking slug
     *
     * @param parkingSlug parking slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String parkingSlug) {
        if (validValidation.isInValid(parkingSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check parking has parking space or not
     *
     * @param parkingSlug parking
     * @return has rooms or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String parkingSlug) {
        final String hqlQuery = "SELECT PS.parkingTypeId " +
                "FROM ParkingEntity P " +
                "JOIN ParkingSpaceEntity PS ON P.id = PS.parkingId " +
                "WHERE P.slug = :parkingSlug AND P.isDeleted IS NULL AND P.isDeleted IS NULL";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "parkingSlug",
                            parkingSlug
                    )
                    .getResultList()
                    .isEmpty();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking",
                    "parking slug",
                    parkingSlug
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
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
