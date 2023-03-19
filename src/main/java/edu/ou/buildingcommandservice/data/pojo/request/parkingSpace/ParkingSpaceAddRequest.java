package edu.ou.buildingcommandservice.data.pojo.request.parkingSpace;

import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ParkingSpaceAddRequest implements IBaseRequest {
    @NotNull
    private ParkingSpaceEntityPK referenceKey;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int capacity = 1;
}
