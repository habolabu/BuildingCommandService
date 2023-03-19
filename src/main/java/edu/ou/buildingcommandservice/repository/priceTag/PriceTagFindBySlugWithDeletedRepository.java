package edu.ou.buildingcommandservice.repository.priceTag;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.PriceTagEntity;
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
public class PriceTagFindBySlugWithDeletedRepository extends BaseRepository<String, PriceTagEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
    }

    /**
     * Find price tag by slug with deleted
     *
     * @param priceTagSlug price tag slug
     * @return price tag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PriceTagEntity doExecute(String priceTagSlug) {
        final String hqlQuery = "FROM PriceTagEntity P WHERE P.slug = :priceTagSlug";

        try {
            return (PriceTagEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "priceTagSlug",
                                    priceTagSlug
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "price tag",
                    "price tag slug",
                    priceTagSlug
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
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
