package edu.ou.buildingcommandservice.service.parking;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ParkingEntity;
import edu.ou.buildingcommandservice.data.pojo.request.parking.ParkingDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.parking.ParkingDeleteQueueI;
import edu.ou.coreservice.queue.human.external.parkingDetail.ParkingDetailCheckEmptyQueueE;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<String, String> parkingDeleteRepository;
    private final IBaseRepository<String, Boolean> parkingCheckExistBySlugRepository;
    private final IBaseRepository<String, Boolean> parkingHasParkingSpacesRepository;
    private final IBaseRepository<String, ParkingEntity> parkingFindBySlugWithoutDeletedRepository;
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
        if (validValidation.isInValid(request, ParkingDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking"
            );
        }
    }

    /**
     * Delete exist parking
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingDeleteRequest parkingDeleteRequest = (ParkingDeleteRequest) request;

        if (!parkingCheckExistBySlugRepository.execute(parkingDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "parking",
                    "parking slug",
                    parkingDeleteRequest.getSlug()
            );
        }

        if (parkingHasParkingSpacesRepository.execute(parkingDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_EMPTY,
                    Message.Error.NOT_EMPTY,
                    "parking"
            );
        }

        final ParkingEntity existParking = parkingFindBySlugWithoutDeletedRepository.execute(parkingDeleteRequest.getSlug());

        final Object parkingIsEmpty = rabbitTemplate.convertSendAndReceive(
                ParkingDetailCheckEmptyQueueE.EXCHANGE,
                ParkingDetailCheckEmptyQueueE.ROUTING_KEY,
                existParking.getId()
        );

        if (Objects.nonNull(parkingIsEmpty)
                && !(boolean) parkingIsEmpty) {
            throw new BusinessException(
                    CodeStatus.NOT_EMPTY,
                    Message.Error.NOT_EMPTY,
                    "parking"
            );
        }

        final String resultSlug = parkingDeleteRepository.execute(parkingDeleteRequest.getSlug());
        if (Objects.isNull(resultSlug)) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.DELETE_FAIL,
                    "parking",
                    "parking slug",
                    parkingDeleteRequest.getSlug()
            );
        }

        rabbitTemplate.convertSendAndReceive(
                ParkingDeleteQueueI.EXCHANGE,
                ParkingDeleteQueueI.ROUTING_KEY,
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
