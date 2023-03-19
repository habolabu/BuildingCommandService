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
public class ApartmentFindBySlugWithDeletedRepository extends BaseRepository<String, ApartmentEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
    }

    /**
     * Find apartment entity by slug (contains deleted apartment)
     *
     * @param apartmentSlug slug of apartment
     * @return apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ApartmentEntity doExecute(String apartmentSlug) {
        final String hqlQuery = "FROM ApartmentEntity A WHERE A.slug = :apartmentSlug";

        try {
            return (ApartmentEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "apartmentSlug",
                                    apartmentSlug
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "apartment",
                    "apartment slug",
                    apartmentSlug
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
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
