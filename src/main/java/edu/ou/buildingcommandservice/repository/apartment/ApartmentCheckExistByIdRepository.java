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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ApartmentCheckExistByIdRepository extends BaseRepository<Integer, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate apartment id
     *
     * @param apartmentId id of apartment
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
     * Check apartment exist or not by id
     *
     * @param apartmentId id of apartment
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(Integer apartmentId) {
        final String hqlQuery = "FROM ApartmentEntity A WHERE A.id = :apartmentId AND A.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "apartmentId",
                            apartmentId
                    )
                    .getSingleResult();
            return true;

        } catch (NoResultException e) {
            return false;

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.SERVER_ERROR,
                    Message.Error.UN_KNOWN
            );

        }
    }

    /**
     * Close connection
     *
     * @param apartmentId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer apartmentId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
