package edu.ou.buildingcommandservice.repository.apartment;


import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ApartmentEntity;
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
public class ApartmentAddRepository extends BaseRepository<ApartmentEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate apartment entity
     *
     * @param apartmentEntity apartment entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ApartmentEntity apartmentEntity) {
        if (validValidation.isInValid(apartmentEntity, ApartmentEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new apartment entity
     *
     * @param apartmentEntity input of task
     * @return id of apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ApartmentEntity apartmentEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(apartmentEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
