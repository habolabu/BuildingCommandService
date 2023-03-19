package edu.ou.buildingcommandservice.data.pojo.request.parking;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ParkingAddRequest implements IBaseRequest {
    @NotBlank
    @Size(
            min = 1,
            max = 50
    )
    private String name;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int apartmentId;
}
