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
public class ApartmentGetFloorAmountByIdRepository extends BaseRepository<Integer, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate apartment id
     *
     * @param apartmentId apartment id
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer apartmentId) {
        if (validValidation.isInValid(apartmentId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Get floor amount by id
     *
     * @param apartmentId apartment id
     * @return floor amount
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(Integer apartmentId) {
        final String hqlQuery = "FROM ApartmentEntity A WHERE A.id = :apartmentId AND A.isDeleted IS NULL";

        try {
            final ApartmentEntity apartmentResult =
                    (ApartmentEntity)
                            entityManager
                                    .unwrap(Session.class)
                                    .createQuery(hqlQuery)
                                    .setParameter(
                                            "apartmentId",
                                            apartmentId
                                    )
                                    .getSingleResult();
            return apartmentResult.getFloorAmount();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "apartment",
                    "apartment identity",
                    apartmentId
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
    protected void postExecute(Integer input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
