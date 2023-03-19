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
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

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
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist parking
     *
     * @param parkingSlug slug of exist parking
     * @return slug of exist parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String parkingSlug) {
        final String hqlQuery =
                "UPDATE ParkingEntity P " +
                        "SET P.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE P.slug = :parkingSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "parkingSlug",
                                    parkingSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return parkingSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "parking",
                    "parking slug",
                    parkingSlug
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
