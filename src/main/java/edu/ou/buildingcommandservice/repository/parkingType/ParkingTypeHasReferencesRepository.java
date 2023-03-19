package edu.ou.buildingcommandservice.repository.parkingType;

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
public class ParkingTypeHasReferencesRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking type slug
     *
     * @param parkingTypeSlug parking type slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String parkingTypeSlug) {
        if (validValidation.isInValid(parkingTypeSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check parking type has references or not
     *
     * @param parkingTypeSlug parking type
     * @return has references or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String parkingTypeSlug) {
        final String hqlQuery = "SELECT PS.id " +
                "FROM ParkingTypeEntity P " +
                "JOIN ParkingSpaceEntity PS ON P.id = PS.parkingTypeId " +
                "WHERE P.slug = :parkingTypeSlug AND P.isDeleted IS NULL";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "parkingTypeSlug",
                            parkingTypeSlug
                    )
                    .getResultList()
                    .isEmpty();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking type",
                    "parking type slug",
                    parkingTypeSlug
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
