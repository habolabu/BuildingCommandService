package edu.ou.buildingcommandservice.repository.apartment;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.ApartmentEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApartmentDeActiveRepository extends BaseRepository<ApartmentEntity, Integer> {
    private final IBaseRepository<ApartmentEntity, Integer> apartmentUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate apartment
     *
     * @param apartment apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ApartmentEntity apartment) {
        if (validValidation.isInValid(apartment, ApartmentEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "apartment"
            );
        }

    }

    /**
     * de-active exist apartment
     *
     * @param apartment apartment
     * @return id of apartment
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ApartmentEntity apartment) {
        apartment.setIsDeleted(null);

        return apartmentUpdateRepository.execute(apartment);
    }

    @Override
    protected void postExecute(ApartmentEntity input) {
        // do nothing
    }
}
