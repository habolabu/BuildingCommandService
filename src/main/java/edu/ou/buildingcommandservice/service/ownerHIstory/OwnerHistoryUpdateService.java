package edu.ou.buildingcommandservice.service.ownerHIstory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.OwnerHistoryEntityMapper;
import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntity;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryUpdateRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.ownerHistory.OwnerHistoryUpdateQueueI;
import edu.ou.coreservice.queue.human.external.user.UserCheckExistQueueE;
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
public class OwnerHistoryUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<OwnerHistoryEntity, Integer> ownerHistoryUpdateRepository;
    private final IBaseRepository<Integer, Boolean> ownerHistoryCheckExistByIdRepository;
    private final IBaseRepository<Integer, Boolean> roomCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, OwnerHistoryUpdateRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history"

            );
        }
    }

    /**
     * Update exist owner history
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final OwnerHistoryEntity ownerHistoryEntity = OwnerHistoryEntityMapper.INSTANCE
                .fromOwnerHistoryUpdateRequest((OwnerHistoryUpdateRequest) request);

        if (!roomCheckExistByIdRepository.execute(ownerHistoryEntity.getRoomId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "room",
                    "room identity",
                    ownerHistoryEntity.getRoomId()
            );
        }

        final Object isOwnerExist = rabbitTemplate.convertSendAndReceive(
                UserCheckExistQueueE.EXCHANGE,
                UserCheckExistQueueE.ROUTING_KEY,
                ownerHistoryEntity.getOwnerId()
        );
        if (Objects.isNull(isOwnerExist)
                || !(boolean) isOwnerExist) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "owner",
                    "owner identity",
                    ownerHistoryEntity.getOwnerId()
            );
        }

        if (!ownerHistoryCheckExistByIdRepository.execute(ownerHistoryEntity.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "owner history",
                    "owner history identity",
                    ownerHistoryEntity.getId()
            );
        }

        final int ownerHistoryId = ownerHistoryUpdateRepository.execute(ownerHistoryEntity);

        rabbitTemplate.convertSendAndReceive(
                OwnerHistoryUpdateQueueI.EXCHANGE,
                OwnerHistoryUpdateQueueI.ROUTING_KEY,
                ownerHistoryEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        ownerHistoryId,
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
