package edu.ou.buildingcommandservice.data.pojo.request.parkingType;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ParkingTypeDeleteRequest implements IBaseRequest {
    @NotBlank
    private String slug;
}
