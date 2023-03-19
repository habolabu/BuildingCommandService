package edu.ou.buildingcommandservice.service.ownerHIstory;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.ownerHistory.OwnerHistoryDeleteQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnerHistoryDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, Integer> ownerHistoryDeleteRepository;
    private final IBaseRepository<Integer, Boolean> ownerHistoryCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, OwnerHistoryDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "owner history"
            );
        }
    }

    /**
     * Delete exist owner history
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final OwnerHistoryDeleteRequest ownerHistoryDeleteRequest = (OwnerHistoryDeleteRequest) request;

        if (!ownerHistoryCheckExistByIdRepository.execute(ownerHistoryDeleteRequest.getId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "owner history",
                    "owner history identity",
                    ownerHistoryDeleteRequest.getId()
            );
        }

        final int resultId = ownerHistoryDeleteRepository.execute(ownerHistoryDeleteRequest.getId());

        rabbitTemplate.convertSendAndReceive(
                OwnerHistoryDeleteQueueI.EXCHANGE,
                OwnerHistoryDeleteQueueI.ROUTING_KEY,
                resultId
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        resultId,
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
