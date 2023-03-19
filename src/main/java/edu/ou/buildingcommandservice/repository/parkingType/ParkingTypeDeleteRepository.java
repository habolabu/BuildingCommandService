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
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingTypeDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

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
                    "parking type slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist parking type
     *
     * @param parkingTypeSlug slug of exist parking type
     * @return slug of exist parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String parkingTypeSlug) {
        final String hqlQuery =
                "UPDATE ParkingTypeEntity P " +
                        "SET P.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE P.slug = :parkingTypeSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "parkingTypeSlug",
                                    parkingTypeSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return parkingTypeSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "parking type",
                    "parking type slug",
                    parkingTypeSlug
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
