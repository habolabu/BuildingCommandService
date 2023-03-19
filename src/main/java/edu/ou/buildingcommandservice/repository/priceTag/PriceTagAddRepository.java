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
public class PriceTagAddRepository extends BaseRepository<PriceTagEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate priceTag
     *
     * @param priceTag priceTag
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
    }

    /**
     * Insert new priceTag
     *
     * @param priceTag new priceTag
     * @return id of new priceTag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PriceTagEntity priceTag) {

        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(priceTag);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
