package edu.ou.buildingcommandservice.repository.parking;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ParkingDeActiveRepository extends BaseRepository<ParkingEntity, Integer> {
    private final BaseRepository<ParkingEntity, Integer> parkingUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate parking
     *
     * @param parkingEntity apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingEntity parkingEntity) {
        if (validValidation.isInValid(parkingEntity, ParkingEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking"
            );
        }

    }

    /**
     * de-active exist parking
     *
     * @param parking parking
     * @return id of parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingEntity parking) {
        parking.setIsDeleted(null);

        return parkingUpdateRepository.execute(parking);
    }

    @Override
    protected void postExecute(ParkingEntity input) {
        // do nothing
    }
}
