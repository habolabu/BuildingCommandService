package edu.ou.buildingcommandservice.repository.priceTag;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.PriceTagEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PriceTagUpdateRepository extends BaseRepository<PriceTagEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * validate price tag
     *
     * @param priceTag price tag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PriceTagEntity priceTag) {
        if (validValidation.isInValid(priceTag, PriceTagEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "price tag"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist price tag
     *
     * @param priceTag exist price tag
     * @return id of exist price tag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PriceTagEntity priceTag) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            PriceTagEntity.class,
                            priceTag.getId()
                    )
                    .setName(priceTag.getName())
                    .setSlug(priceTag.getSlug())
                    .setPricePerDay(priceTag.getPricePerDay())
                    .setIsDeleted(priceTag.getIsDeleted());
            entityTransaction.commit();

            return priceTag.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "price tag"
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
    protected void postExecute(PriceTagEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
