package edu.ou.buildingcommandservice.service.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.ParkingSpaceEntityMapper;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntity;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.buildingcommandservice.data.pojo.request.parkingSpace.ParkingSpaceUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.parkingSpace.ParkingSpaceUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingSpaceUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ParkingSpaceEntity, ParkingSpaceEntityPK> parkingSpaceUpdateRepository;
    private final IBaseRepository<ParkingSpaceEntityPK, Boolean> parkingSpaceCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> parkingCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> parkingTypeCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, ParkingSpaceUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking space"
            );
        }
    }

    /**
     * Update exist parking space
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingSpaceUpdateRequest parkingSpaceUpdateRequest = (ParkingSpaceUpdateRequest) request;
        final ParkingSpaceEntity parkingSpaceEntity = ParkingSpaceEntityMapper.INSTANCE
                .fromParkingSpaceUpdateRequest(parkingSpaceUpdateRequest);

        if (!parkingSpaceCheckExistByIdRepository.execute(parkingSpaceUpdateRequest.getReferenceKey())
                || !parkingCheckExistByIdRepository.execute(parkingSpaceEntity.getParkingId())
                || !parkingTypeCheckExistByIdRepository.execute(parkingSpaceEntity.getParkingTypeId())
        ) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking and parking type",
                    "parking identity and parking type identity",
                    String.format("%d, %d", parkingSpaceEntity.getParkingId(), parkingSpaceEntity.getParkingTypeId())
            );
        }

        final ParkingSpaceEntityPK parkingSpaceId = parkingSpaceUpdateRepository.execute(parkingSpaceEntity);

        rabbitTemplate.convertSendAndReceive(
                ParkingSpaceUpdateQueueI.EXCHANGE,
                ParkingSpaceUpdateQueueI.ROUTING_KEY,
                parkingSpaceEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        parkingSpaceId,
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
