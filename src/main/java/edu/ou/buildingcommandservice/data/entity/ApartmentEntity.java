package edu.ou.buildingcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "Apartment",
        schema = "BuildingCommandService"
)
public class ApartmentEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Size(
            min = 1,
            max = 50,
            message = "Length must be in range 1 - 50"
    )
    @Column(name = "name")
    private String name;

    @Basic
    @Size(
            min = 1,
            max = 50,
            message = "Length must be in range 1 - 50"
    )
    @Column(name = "slug")
    private String slug;

    @Basic
    @Min(
            value = 0,
            message = "The value must be positive"
    )
    @Max(
            value = 100,
            message = "The value must be equals or less than 100"
    )
    @Column(name = "floorAmount")
    private int floorAmount;

    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;

    @Basic
    @NotNull
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "areaId")
    private Integer areaId;

}
