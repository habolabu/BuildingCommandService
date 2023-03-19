package edu.ou.buildingcommandservice.repository.priceTag;

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
public class PriceTagDeleteRepository extends BaseRepository<String, String> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate price tag slug
     *
     * @param priceTagSlug price tag slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String priceTagSlug) {
        if (validValidation.isInValid(priceTagSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "price tag slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Delete exist price tag
     *
     * @param priceTagSlug slug of price tag which want to delete
     * @return slug of deleted price tag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected String doExecute(String priceTagSlug) {
        final String hqlQuery =
                "UPDATE PriceTagEntity P " +
                        "SET P.isDeleted = CURRENT_TIMESTAMP " +
                        "WHERE P.slug = :priceTagSlug";

        try {
            entityTransaction.begin();
            final int rowChangeNumber =
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "priceTagSlug",
                                    priceTagSlug
                            )
                            .executeUpdate();
            entityTransaction.commit();

            if (rowChangeNumber == 1) {
                return priceTagSlug;
            }
            return null;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "price tag",
                    "price tag slug",
                    priceTagSlug
            );

        }
    }

    /**
     * Close connection
     *
     * @param areaSlug input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(String areaSlug) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
