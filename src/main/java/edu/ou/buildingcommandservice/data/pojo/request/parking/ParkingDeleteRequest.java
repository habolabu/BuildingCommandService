package edu.ou.buildingcommandservice.data.pojo.request.parking;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ParkingDeleteRequest implements IBaseRequest {
    @NotBlank
    private String slug;
}
