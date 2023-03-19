package edu.ou.buildingcommandservice.service.parkingType;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.pojo.request.parkingType.ParkingTypeDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.parkingType.ParkingTypeDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingTypeDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<String, String> parkingTypeDeleteRepository;
    private final IBaseRepository<String, Boolean> parkingTypeCheckExistBySlugRepository;
    private final IBaseRepository<String, Boolean> parkingTypeHasReferencesRepository;
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
        if (validValidation.isInValid(request, ParkingTypeDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type"

            );
        }
    }

    /**
     * Delete exist parking type
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingTypeDeleteRequest parkingTypeDeleteRequest = (ParkingTypeDeleteRequest) request;

        if (!parkingTypeCheckExistBySlugRepository.execute(parkingTypeDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking type",
                    "parking type slug",
                    parkingTypeDeleteRequest.getSlug()
            );
        }

        if (parkingTypeHasReferencesRepository.execute(parkingTypeDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_EMPTY,
                    Message.Error.NOT_EMPTY,
                    "parking type"
            );
        }

        final String resultSlug = parkingTypeDeleteRepository.execute(parkingTypeDeleteRequest.getSlug());

        rabbitTemplate.convertSendAndReceive(
                ParkingTypeDeleteQueueI.EXCHANGE,
                ParkingTypeDeleteQueueI.ROUTING_KEY,
                resultSlug
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        resultSlug,
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
