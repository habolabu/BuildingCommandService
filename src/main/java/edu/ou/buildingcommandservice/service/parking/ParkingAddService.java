package edu.ou.buildingcommandservice.service.parking;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.ParkingEntityMapper;
import edu.ou.buildingcommandservice.data.entity.ParkingEntity;
import edu.ou.buildingcommandservice.data.pojo.request.parking.ParkingAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.parking.ParkingAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParkingAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ParkingEntity, Integer> parkingAddRepository;
    private final IBaseRepository<String, ParkingEntity> parkingFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> parkingCheckDeleteRepository;
    private final IBaseRepository<ParkingEntity, Integer> parkingDeActiveRepository;
    private final IBaseRepository<Integer, Boolean> apartmentCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, ParkingAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking"

            );
        }
    }

    /**
     * Add new parking
     *
     * @param request request
     * @return id of new parking
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ParkingEntity parkingEntity = ParkingEntityMapper.INSTANCE
                .fromParkingAddRequest((ParkingAddRequest) request);

        if (!apartmentCheckExistByIdRepository.execute(parkingEntity.getApartmentId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "apartment",
                    "apartment identity",
                    parkingEntity.getApartmentId()
            );
        }

        int parkingId;

        if (parkingCheckDeleteRepository.execute(parkingEntity.getSlug())) {
            final ParkingEntity existDeletedParkingEntity =
                    parkingFindBySlugWithDeletedRepository.execute(parkingEntity.getSlug());

            parkingEntity.setId(existDeletedParkingEntity.getId());
            parkingId = parkingDeActiveRepository.execute(parkingEntity);

        } else {
            parkingId = parkingAddRepository.execute(parkingEntity);
        }

        parkingEntity.setId(parkingId);

        rabbitTemplate.convertSendAndReceive(
                ParkingAddQueueI.EXCHANGE,
                ParkingAddQueueI.ROUTING_KEY,
                parkingEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        parkingId,
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
