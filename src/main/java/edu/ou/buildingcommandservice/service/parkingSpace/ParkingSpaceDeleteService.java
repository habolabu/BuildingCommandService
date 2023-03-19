package edu.ou.buildingcommandservice.service.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.buildingcommandservice.data.pojo.request.parkingSpace.ParkingSpaceDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.parkingSpace.ParkingSpaceDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingSpaceDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ParkingSpaceEntityPK, ParkingSpaceEntityPK> parkingSpaceDeleteRepository;
    private final IBaseRepository<ParkingSpaceEntityPK, Boolean> parkingSpaceCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, ParkingSpaceDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking space"
            );
        }
    }

    /**
     * Delete exist parking space
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingSpaceDeleteRequest parkingSpaceDeleteRequest = (ParkingSpaceDeleteRequest) request;

        if (!parkingSpaceCheckExistByIdRepository.execute(parkingSpaceDeleteRequest.getReferenceKey())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking space",
                    "parking space identity",
                    parkingSpaceDeleteRequest.getReferenceKey()
            );
        }

        final ParkingSpaceEntityPK resultPK =
                parkingSpaceDeleteRepository.execute(parkingSpaceDeleteRequest.getReferenceKey());

        rabbitTemplate.convertSendAndReceive(
                ParkingSpaceDeleteQueueI.EXCHANGE,
                ParkingSpaceDeleteQueueI.ROUTING_KEY,
                resultPK
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        resultPK,
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
