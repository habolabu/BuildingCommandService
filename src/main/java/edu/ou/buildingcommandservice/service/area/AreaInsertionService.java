package edu.ou.buildingcommandservice.service.area;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.AreaEntityMapper;
import edu.ou.buildingcommandservice.data.entity.AreaEntity;
import edu.ou.buildingcommandservice.data.pojo.request.area.AreaInsertionRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.area.AreaAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AreaInsertionService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<AreaEntity, Integer> areaInsertionRepository;
    private final IBaseRepository<String, AreaEntity> areaFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> areaCheckDeleteRepository;
    private final IBaseRepository<AreaEntity, Integer> areaDeActiveRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ValidValidation validValidation;

    /**
     * Validate request
     *
     * @param request input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(IBaseRequest request) {
        if (validValidation.isInValid(request, AreaInsertionRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "area"
            );
        }
    }

    /**
     * Insert new area
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final AreaEntity areaEntity = AreaEntityMapper.INSTANCE
                .fromAreaInsertionRequest((AreaInsertionRequest) request);

        int areaId;

        if (areaCheckDeleteRepository.execute(areaEntity.getSlug())) {
            final AreaEntity existDeletedAreaEntity =
                    areaFindBySlugWithDeletedRepository.execute(areaEntity.getSlug());
            areaEntity.setId(existDeletedAreaEntity.getId());
            areaId = areaDeActiveRepository.execute(areaEntity);

        } else {
            areaId = areaInsertionRepository.execute(areaEntity);
        }

        areaEntity.setId(areaId);

        rabbitTemplate.convertSendAndReceive(
                AreaAddQueueI.EXCHANGE,
                AreaAddQueueI.ROUTING_KEY,
                areaEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        areaId,
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
