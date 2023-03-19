package edu.ou.buildingcommandservice.data.pojo.request.parkingSpace;

import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ParkingSpaceDeleteRequest implements IBaseRequest {
    @NotNull
    private ParkingSpaceEntityPK referenceKey;
}
