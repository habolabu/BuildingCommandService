package edu.ou.buildingcommandservice.repository.area;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.AreaEntity;
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
public class AreaUpdateRepository extends BaseRepository<AreaEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * validate area
     *
     * @param area area
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(AreaEntity area) {
        if (validValidation.isInValid(area, AreaEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "area"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist area
     *
     * @param area exist area
     * @return id of exist area
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(AreaEntity area) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            AreaEntity.class,
                            area.getId()
                    )
                    .setName(area.getName())
                    .setAddress(area.getAddress())
                    .setSlug(area.getSlug())
                    .setIsDeleted(area.getIsDeleted());
            entityTransaction.commit();

            return area.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "area"
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
    protected void postExecute(AreaEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
