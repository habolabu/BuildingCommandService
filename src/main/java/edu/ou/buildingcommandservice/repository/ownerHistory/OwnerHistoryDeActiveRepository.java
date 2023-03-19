package edu.ou.buildingcommandservice.repository.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OwnerHistoryDeActiveRepository extends BaseRepository<OwnerHistoryEntity, Integer> {
    private final IBaseRepository<OwnerHistoryEntity, Integer> ownerHistoryUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate ownerHistory
     *
     * @param ownerHistory ownerHistory
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(OwnerHistoryEntity ownerHistory) {
        if (validValidation.isInValid(ownerHistory, OwnerHistoryEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history"
            );
        }

    }

    /**
     * de-active exist ownerHistory
     *
     * @param ownerHistory ownerHistory
     * @return id of ownerHistory
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(OwnerHistoryEntity ownerHistory) {
        ownerHistory.setIsDeleted(null);

        return ownerHistoryUpdateRepository.execute(ownerHistory);
    }

    @Override
    protected void postExecute(OwnerHistoryEntity input) {
        // do nothing
    }
}
