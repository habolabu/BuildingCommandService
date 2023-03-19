package edu.ou.buildingcommandservice.repository.area;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.AreaEntity;
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
public class AreaFindByApartmentSlugRepository extends BaseRepository<String, AreaEntity> {
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
     * Find area by apartment slug
     *
     * @param apartmentSlug apartment slug
     * @return area
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected AreaEntity doExecute(String apartmentSlug) {
        final String hqlQuery = "FROM AreaEntity A " +
                "WHERE A.isDeleted IS NULL AND A.id = " +
                "( " +
                "SELECT AP.areaId " +
                "FROM ApartmentEntity AP " +
                "WHERE AP.isDeleted IS NULL AND AP.slug = :apartmentSlug" +
                " )";

        try {
            return (AreaEntity)
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
