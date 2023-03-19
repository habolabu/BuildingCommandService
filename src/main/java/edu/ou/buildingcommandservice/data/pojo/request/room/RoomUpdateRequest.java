package edu.ou.buildingcommandservice.data.pojo.request.room;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RoomUpdateRequest implements IBaseRequest {
    private int id;

    @NotBlank
    @Size(
            min = 1,
            max = 50
    )
    private String name;

    @Min(
            value = 0,
            message = "The value must be positive"
    )
    @Max(
            value = 100,
            message = "The value must be equals or less than 100"
    )
    private int floorNumber;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int apartmentId;

    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    private int priceTagId;
}
