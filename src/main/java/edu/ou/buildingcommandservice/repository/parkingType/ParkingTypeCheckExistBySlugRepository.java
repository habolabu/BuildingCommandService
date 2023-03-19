package edu.ou.buildingcommandservice.repository.parkingType;

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
public class ParkingTypeCheckExistBySlugRepository extends BaseRepository<String, Boolean> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking type slug
     *
     * @param parkingTypeSlug slug of parking type
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
     * Check parking type exist or not by slug
     *
     * @param parkingTypeSlug slug of parking type
     * @return exist or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String parkingTypeSlug) {
        final String hqlQuery = "FROM ParkingTypeEntity P WHERE P.slug = :parkingTypeSlug AND P.isDeleted IS NULL";

        try {
            entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "parkingTypeSlug",
                            parkingTypeSlug
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
