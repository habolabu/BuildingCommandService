package edu.ou.buildingcommandservice.service.priceTag;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.PriceTagEntityMapper;
import edu.ou.buildingcommandservice.data.entity.PriceTagEntity;
import edu.ou.buildingcommandservice.data.pojo.request.priceTag.PriceTagAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.priceTag.PriceTagAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PriceTagAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<PriceTagEntity, Integer> priceTagAddRepository;
    private final IBaseRepository<String, PriceTagEntity> priceTagFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> priceTagCheckDeleteRepository;
    private final IBaseRepository<PriceTagEntity, Integer> priceTagDeActiveRepository;
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
        if (validValidation.isInValid(request, PriceTagAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "price tag"

            );
        }
    }

    /**
     * Insert new price tag
     *
     * @param request request from client
     * @return response to client
     * v
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final PriceTagEntity priceTagEntity = PriceTagEntityMapper.INSTANCE
                .fromPriceTagAddRequest((PriceTagAddRequest) request);

        int priceTagId;

        if (priceTagCheckDeleteRepository.execute(priceTagEntity.getSlug())) {
            final PriceTagEntity existDeletedPriceTagEntity =
                    priceTagFindBySlugWithDeletedRepository.execute(priceTagEntity.getSlug());

            priceTagEntity.setId(existDeletedPriceTagEntity.getId());
            priceTagId = priceTagDeActiveRepository.execute(priceTagEntity);

        } else {
            priceTagId = priceTagAddRepository.execute(priceTagEntity);
        }

        priceTagEntity.setId(priceTagId);
        rabbitTemplate.convertSendAndReceive(
                PriceTagAddQueueI.EXCHANGE,
                PriceTagAddQueueI.ROUTING_KEY,
                priceTagEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        priceTagId,
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
