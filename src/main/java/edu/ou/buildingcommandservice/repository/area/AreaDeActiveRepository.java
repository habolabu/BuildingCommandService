package edu.ou.buildingcommandservice.repository.area;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.AreaEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AreaDeActiveRepository extends BaseRepository<AreaEntity, Integer> {
    private final BaseRepository<AreaEntity, Integer> areaUpdateRepository;
    private final ValidValidation validValidation;

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
    }

    /**
     * de-active exist area
     *
     * @param area area
     * @return id of area
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(AreaEntity area) {
        area.setIsDeleted(null);

        return areaUpdateRepository.execute(area);
    }

    @Override
    protected void postExecute(AreaEntity input) {
        // do nothing
    }
}
