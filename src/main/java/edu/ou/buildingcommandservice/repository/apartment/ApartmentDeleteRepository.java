package edu.ou.buildingcommandservice.repository.apartment;

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
public class ApartmentDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate apartment slug
     *
     * @param apartmentSlug apartment slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String apartmentSlug) {
        if (validValidation.isInValid(apartmentSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist apartment
     *
     * @param apartmentSlug slug of exist apartment
     * @return slug of exist apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String apartmentSlug) {
        final String hqlQuery =
                "UPDATE ApartmentEntity A " +
                        "SET A.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE A.slug = :apartmentSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "apartmentSlug",
                                    apartmentSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return apartmentSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "apartment",
                    "apartment slug",
                    apartmentSlug
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
