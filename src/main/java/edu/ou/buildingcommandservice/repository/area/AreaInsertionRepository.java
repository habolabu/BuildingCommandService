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
public class AreaInsertionRepository extends BaseRepository<AreaEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate area
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
    }

    /**
     * Insert new area
     *
     * @param area new area
     * @return id of new area
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(AreaEntity area) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(area);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
