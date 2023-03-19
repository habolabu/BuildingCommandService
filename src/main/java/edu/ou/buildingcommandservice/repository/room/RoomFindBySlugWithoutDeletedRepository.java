package edu.ou.buildingcommandservice.repository.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
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
public class RoomFindBySlugWithoutDeletedRepository extends BaseRepository<String, RoomEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate room slug
     *
     * @param roomSlug room slug
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(String roomSlug) {
        if (validValidation.isInValid(roomSlug)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room slug"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find room by room slug
     *
     * @param roomSlug room slug
     * @return room
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected RoomEntity doExecute(String roomSlug) {
        final String hqlQuery = "FROM RoomEntity R WHERE R.slug = :roomSlug AND R.isDeleted IS NULL";

        try {
            return (RoomEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "roomSlug",
                                    roomSlug
                            )
                            .getSingleResult();

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "room",
                    "room slug",
                    roomSlug
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
