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
public class ParkingCheckDeleteRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking slug
     *
     * @param parkingSlug slug of parking
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
     * Check deleted status of parking
     *
     * @param parkingSlug slug of parking
     * @return deleted status
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String parkingSlug) {
        final String hqlQuery = "FROM ParkingEntity P WHERE P.slug = :parkingSlug AND P.isDeleted IS NOT NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "parkingSlug",
                            parkingSlug
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
     * @param apartmentSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String apartmentSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
