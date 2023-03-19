package edu.ou.buildingcommandservice.repository.parkingType;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingTypeEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ParkingTypeDeActiveRepository extends BaseRepository<ParkingTypeEntity, Integer> {
    private final BaseRepository<ParkingTypeEntity, Integer> parkingTypeUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate parking type
     *
     * @param parkingType parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingTypeEntity parkingType) {
        if (validValidation.isInValid(parkingType, ParkingTypeEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type"
            );
        }

    }

    /**
     * de-active exist parking type
     *
     * @param parkingType parking type
     * @return id of parking type
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingTypeEntity parkingType) {
        parkingType.setIsDeleted(null);

        return parkingTypeUpdateRepository.execute(parkingType);
    }

    @Override
    protected void postExecute(ParkingTypeEntity input) {
        // do nothing
    }
}
