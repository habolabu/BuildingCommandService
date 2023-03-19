package edu.ou.buildingcommandservice.data.pojo.request.ownerHistory;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class OwnerHistoryAddRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int roomId;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int ownerId;
}
