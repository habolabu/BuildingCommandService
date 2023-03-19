package edu.ou.buildingcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
@Data
@Table(
        name = "ParkingSpace",
        schema = "BuildingCommandService"
)
@IdClass(ParkingSpaceEntityPK.class)
public class ParkingSpaceEntity implements Serializable {
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

    @Basic
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "capacity")
    private Integer capacity = 1;

    @Basic
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "availableSpace")
    private Integer availableSpace = 1;
}
