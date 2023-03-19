package edu.ou.buildingcommandservice.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class ParkingSpaceEntityPK implements Serializable {
    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "parkingId")
    private int parkingId;

    @Id
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "parkingTypeId")
    private int parkingTypeId;
}
