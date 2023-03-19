package edu.ou.buildingcommandservice.service.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.RoomEntityMapper;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
import edu.ou.buildingcommandservice.data.pojo.request.room.RoomAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.room.RoomAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<RoomEntity, Integer> roomAddRepository;
    private final IBaseRepository<String, RoomEntity> roomFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> roomCheckDeleteRepository;
    private final IBaseRepository<RoomEntity, Integer> roomDeActiveRepository;
    private final IBaseRepository<Integer, Boolean> apartmentCheckExistByIdRepository;
    private final IBaseRepository<Integer, Integer> apartmentGetFloorAmountByIdRepository;
    private final IBaseRepository<Integer, Boolean> priceTagCheckExistByIdRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request request
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, RoomAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room"
            );
        }
    }

    /**
     * Add new room
     *
     * @param request request
     * @return id of new room
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final RoomEntity roomEntity = RoomEntityMapper.INSTANCE.fromRoomAddRequest((RoomAddRequest) request);

        if (!apartmentCheckExistByIdRepository.execute(roomEntity.getApartmentId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "apartment",
                    "apartment identity",
                    roomEntity.getApartmentId()
            );
        }

        if (!priceTagCheckExistByIdRepository.execute(roomEntity.getPriceTagId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "price tag",
                    "price tag identity",
                    roomEntity.getPriceTagId()
            );
        }

        if (roomEntity.getFloorNumber() > apartmentGetFloorAmountByIdRepository.execute(roomEntity.getApartmentId())) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "floor number"
            );
        }

        int roomId;

        if (roomCheckDeleteRepository.execute(roomEntity.getSlug())) {
            final RoomEntity existDeletedRoomEntity =
                    roomFindBySlugWithDeletedRepository.execute(roomEntity.getSlug());

            roomEntity.setId(existDeletedRoomEntity.getId());
            roomId = roomDeActiveRepository.execute(roomEntity);

        } else {
            roomId = roomAddRepository.execute(roomEntity);

        }

        roomEntity.setId(roomId);
        rabbitTemplate.convertSendAndReceive(
                RoomAddQueueI.EXCHANGE,
                RoomAddQueueI.ROUTING_KEY,
                roomEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        roomId,
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}
