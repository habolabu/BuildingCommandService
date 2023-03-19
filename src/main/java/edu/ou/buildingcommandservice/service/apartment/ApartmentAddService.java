package edu.ou.buildingcommandservice.service.apartment;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.common.mapper.ApartmentEntityMapper;
import edu.ou.buildingcommandservice.data.entity.ApartmentEntity;
import edu.ou.buildingcommandservice.data.pojo.request.apartment.ApartmentAddRequest;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.building.internal.apartment.ApartmentAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApartmentAddService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<ApartmentEntity, Integer> apartmentAddRepository;
    private final IBaseRepository<String, ApartmentEntity> apartmentFindBySlugWithDeletedRepository;
    private final IBaseRepository<String, Boolean> apartmentCheckDeleteRepository;
    private final IBaseRepository<ApartmentEntity, Integer> apartmentDeActiveRepository;
    private final IBaseRepository<Integer, Boolean> areaCheckExistByIdRepository;
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
        if (validValidation.isInValid(request, ApartmentAddRequest.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment"

            );
        }
    }

    /**
     * Add new apartment
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final ApartmentEntity apartmentEntity = ApartmentEntityMapper.INSTANCE
                .fromApartmentAddRequest((ApartmentAddRequest) request);

        if (!areaCheckExistByIdRepository.execute(apartmentEntity.getAreaId())) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "area",
                    "area identity",
                    apartmentEntity.getAreaId().toString()
            );
        }

        int apartmentId;

        if (apartmentCheckDeleteRepository.execute(apartmentEntity.getSlug())) {
            final ApartmentEntity existDeletedApartmentEntity =
                    apartmentFindBySlugWithDeletedRepository.execute(apartmentEntity.getSlug());

            apartmentEntity.setId(existDeletedApartmentEntity.getId());
            apartmentId = apartmentDeActiveRepository.execute(apartmentEntity);

        } else {
            apartmentId = apartmentAddRepository.execute(apartmentEntity);
        }

        apartmentEntity.setId(apartmentId);

        rabbitTemplate.convertSendAndReceive(
                ApartmentAddQueueI.EXCHANGE,
                ApartmentAddQueueI.ROUTING_KEY,
                apartmentEntity
        );

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        apartmentId,
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
