package edu.ou.buildingcommandservice.service.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.RoomEntityMapper;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
import edu.ou.buildingcommandservice.data.pojo.request.room.RoomUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.room.RoomUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<RoomEntity, Integer> roomUpdateRepository;
    private final IBaseRepository<Integer, Boolean> roomCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, RoomUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room"
            );
        }
    }

    /**
     * Update exist room
     *
     * @param request request
     * @return response
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final RoomEntity roomEntity = RoomEntityMapper.INSTANCE
                .fromRoomUpdateRequest((RoomUpdateRequest) request);

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

        if (!roomCheckExistByIdRepository.execute(roomEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "room",
                    "room identity",
                    roomEntity.getId()
            );
        }

        if (roomEntity.getFloorNumber() > apartmentGetFloorAmountByIdRepository.execute(roomEntity.getApartmentId())) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "floor number"
            );
        }

        final int roomId = roomUpdateRepository.execute(roomEntity);

        rabbitTemplate.convertSendAndReceive(
                RoomUpdateQueueI.EXCHANGE,
                RoomUpdateQueueI.ROUTING_KEY,
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
