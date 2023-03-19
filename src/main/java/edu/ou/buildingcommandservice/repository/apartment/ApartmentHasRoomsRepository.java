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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ApartmentHasRoomsRepository extends BaseRepository<String, Boolean> {
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
     * Check apartments has rooms or not
     *
     * @param apartmentSlug apartment
     * @return has rooms or not
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Boolean doExecute(String apartmentSlug) {
        final String hqlQuery = "SELECT R.id " +
                "FROM ApartmentEntity A " +
                "JOIN RoomEntity R ON A.id = R.apartmentId " +
                "WHERE A.slug = :apartmentSlug AND A.isDeleted IS NULL AND R.isDeleted IS NULL";

        try {
            return !entityManager
                    .unwrap(Session.class)
                    .createQuery(hqlQuery)
                    .setParameter(
                            "apartmentSlug",
                            apartmentSlug
                    )
                    .getResultList()
                    .isEmpty();

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

    @Override
    protected void postExecute(String input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
