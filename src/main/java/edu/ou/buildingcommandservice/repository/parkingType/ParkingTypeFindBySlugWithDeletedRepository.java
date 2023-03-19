package edu.ou.buildingcommandservice.repository.parkingType;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingTypeEntity;
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
public class ParkingTypeFindBySlugWithDeletedRepository
        extends BaseRepository<String, ParkingTypeEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking type slug
     *
     * @param parkingTypeSlug parking type slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String parkingTypeSlug) {
        if (validValidation.isInValid(parkingTypeSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find parking type entity by slug (contains deleted parking type)
     *
     * @param parkingTypeSlug slug of parking type
     * @return parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected ParkingTypeEntity doExecute(String parkingTypeSlug) {
        final String hqlQuery = "FROM ParkingTypeEntity P WHERE P.slug = :parkingTypeSlug";

        try {
            return (ParkingTypeEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "parkingTypeSlug",
                                    parkingTypeSlug
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking type",
                    "parking type slug",
                    parkingTypeSlug
            );

        }
    }

    /**
     * Connection close
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
