package edu.ou.buildingcommandservice.service.parkingType;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.ParkingTypeEntityMapper;
import edu.ou.buildingcommandservice.data.entity.ParkingTypeEntity;
import edu.ou.buildingcommandservice.data.pojo.request.parkingType.ParkingTypeAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.parkingType.ParkingTypeAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingTypeAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ParkingTypeEntity, Integer> parkingTypeAddRepository;
    private final IBaseRepository<String, ParkingTypeEntity> parkingTypeFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> parkingTypeCheckDeleteRepository;
    private final IBaseRepository<ParkingTypeEntity, Integer> parkingTypeDeActiveRepository;
    private final IBaseRepository<Integer, Boolean> priceTagCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, ParkingTypeAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking type"
            );
        }
    }

    /**
     * Add new parking type
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingTypeEntity parkingTypeEntity = ParkingTypeEntityMapper.INSTANCE
                .fromParkingTypeAddRequest((ParkingTypeAddRequest) request);

        if (!priceTagCheckExistByIdRepository.execute(parkingTypeEntity.getPriceTagId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "price tag",
                    "price tag identity",
                    parkingTypeEntity.getPriceTagId()
            );
        }

        int parkingTypeId;

        if (parkingTypeCheckDeleteRepository.execute(parkingTypeEntity.getSlug())) {
            final ParkingTypeEntity existDeletedParkingTypeEntity =
                    parkingTypeFindBySlugWithDeletedRepository.execute(parkingTypeEntity.getSlug());

            parkingTypeEntity.setId(existDeletedParkingTypeEntity.getId());
            parkingTypeId = parkingTypeDeActiveRepository.execute(parkingTypeEntity);

        } else {
            parkingTypeId = parkingTypeAddRepository.execute(parkingTypeEntity);
        }

        parkingTypeEntity.setId(parkingTypeId);

        rabbitTemplate.convertSendAndReceive(
                ParkingTypeAddQueueI.EXCHANGE,
                ParkingTypeAddQueueI.ROUTING_KEY,
                parkingTypeEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        parkingTypeId,
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
