package edu.ou.buildingcommandservice.repository.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomDeActiveRepository extends BaseRepository<RoomEntity, Integer> {
    private final BaseRepository<RoomEntity, Integer> roomUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate room
     *
     * @param room room
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(RoomEntity room) {
        if (validValidation.isInValid(room, RoomEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room"
            );
        }
    }

    /**
     * De-active exist room
     *
     * @param room room
     * @return id of room
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(RoomEntity room) {
        room.setIsDeleted(null);

        return roomUpdateRepository.execute(room);
    }

    @Override
    protected void postExecute(RoomEntity input) {
        // do nothing
    }
}
