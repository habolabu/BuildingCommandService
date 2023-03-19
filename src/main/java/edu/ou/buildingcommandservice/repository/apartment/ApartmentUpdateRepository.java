package edu.ou.buildingcommandservice.repository.apartment;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ApartmentEntity;
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
public class ApartmentUpdateRepository extends BaseRepository<ApartmentEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate apartment
     *
     * @param apartment apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ApartmentEntity apartment) {
        if (validValidation.isInValid(apartment, ApartmentEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist apartment
     *
     * @param apartment apartment information
     * @return id of updated apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ApartmentEntity apartment) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            ApartmentEntity.class,
                            apartment.getId()
                    )
                    .setName(apartment.getName())
                    .setAreaId(apartment.getAreaId())
                    .setSlug(apartment.getSlug())
                    .setFloorAmount(apartment.getFloorAmount())
                    .setIsDeleted(apartment.getIsDeleted());
            entityTransaction.commit();

            return apartment.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "apartment"
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
    protected void postExecute(ApartmentEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
