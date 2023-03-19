package edu.ou.buildingcommandservice.service.room;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.RoomEntity;
import edu.ou.buildingcommandservice.data.pojo.request.room.RoomDeleteRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.room.RoomDeleteQueueI;
import edu.ou.coreservice.queue.human.external.roomDetail.RoomDetailCheckEmptyQueueE;
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
public class RoomDeleteService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<String, String> roomDeleteRepository;
    private final IBaseRepository<String, Boolean> roomCheckExistBySlugRepository;
    private final IBaseRepository<String, RoomEntity> roomFindBySlugWithoutDeletedRepository;
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
        if (validValidation.isInValid(request, RoomDeleteRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room"
            );
        }
    }

    /**
     * Delete exit room
     *
     * @param request request
     * @return response
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final RoomDeleteRequest roomDeleteRequest = (RoomDeleteRequest) request;

        if (!roomCheckExistBySlugRepository.execute(roomDeleteRequest.getSlug())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "room",
                    "room slug",
                    roomDeleteRequest.getSlug()
            );
        }

        final RoomEntity existRoom = roomFindBySlugWithoutDeletedRepository.execute(roomDeleteRequest.getSlug());

        final Object roomIsEmpty = rabbitTemplate.convertSendAndReceive(
                RoomDetailCheckEmptyQueueE.EXCHANGE,
                RoomDetailCheckEmptyQueueE.ROUTING_KEY,
                existRoom.getId()
        );
        if (Objects.nonNull(roomIsEmpty)
                && !(boolean) roomIsEmpty) {
            throw new BusinessException(
                    CodeStatus.NOT_EMPTY,
                    Message.Error.NOT_EMPTY,
                    "room"
            );
        }

        final String resultSlug = roomDeleteRepository.execute(roomDeleteRequest.getSlug());

        rabbitTemplate.convertSendAndReceive(
                RoomDeleteQueueI.EXCHANGE,
                RoomDeleteQueueI.ROUTING_KEY,
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
