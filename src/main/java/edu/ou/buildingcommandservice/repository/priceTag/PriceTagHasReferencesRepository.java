package edu.ou.buildingcommandservice.repository.priceTag;

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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PriceTagHasReferencesRepository extends BaseRepository<String, Boolean> {
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
                    "price tag"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Check references of price tag
     *
     * @param priceTagSlug price tag slug
     * @return area has references or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String priceTagSlug) {
        final String hqlQuery = "SELECT P.id " +
                "FROM PriceTagEntity P " +
                "JOIN ParkingTypeEntity PT ON P.id = PT.priceTagId " +
                "JOIN RoomEntity R ON P.id = R.priceTagId " +
                "WHERE P.slug = :priceTagSlug AND P.isDeleted IS NULL " +
                "AND PT.isDeleted IS NULL AND R.isDeleted IS NULL ";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "priceTagSlug",
                            priceTagSlug
                    )
                    .getResultList()
                    .isEmpty();

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
