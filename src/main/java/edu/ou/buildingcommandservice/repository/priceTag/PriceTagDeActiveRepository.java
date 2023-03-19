package edu.ou.buildingcommandservice.repository.priceTag;

import edu.ou.buildingcommandservice.common.constant.CodeStatus;
import edu.ou.buildingcommandservice.data.entity.PriceTagEntity;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.coreservice.repository.base.IBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PriceTagDeActiveRepository extends BaseRepository<PriceTagEntity, Integer> {
    private final IBaseRepository<PriceTagEntity, Integer> priceTagUpdateRepository;
    private final ValidValidation validValidation;

    /**
     * Validate priceTag
     *
     * @param priceTag priceTag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PriceTagEntity priceTag) {
        if (validValidation.isInValid(priceTag, PriceTagEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "price tag"
            );
        }
    }

    /**
     * de-active exist price tag
     *
     * @param priceTag price tag
     * @return id of price tag
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PriceTagEntity priceTag) {
        priceTag.setIsDeleted(null);

        return priceTagUpdateRepository.execute(priceTag);
    }

    @Override
    protected void postExecute(PriceTagEntity input) {
        // do nothing
    }
}
