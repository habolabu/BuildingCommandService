package edu.ou.buildingcommandservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "Parking",
        schema = "BuildingCommandService"
)
public class ParkingEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 50,
            message = "Length must be in range 1 - 50"
    )
    @Column(name = "name")
    private String name;

    @Basic
    @NotBlank
    @Size(
            min = 1,
            max = 50,
            message = "Length must be in range 1 - 50"
    )
    @Column(name = "slug")
    private String slug;

    @Basic
    @Column(name = "isDeleted")
    @JsonIgnore
    private Timestamp isDeleted;

    @Basic
    @NotNull
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "apartmentId")
    private int apartmentId;
}
