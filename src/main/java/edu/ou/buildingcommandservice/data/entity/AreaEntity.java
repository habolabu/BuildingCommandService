package edu.ou.buildingcommandservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "Area",
        schema = "BuildingCommandService"
)
public class AreaEntity implements Serializable {
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
    @Size(
            max = 255,
            message = "Length must less or equals than 255"
    )
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "isDeleted")
    @JsonIgnore
    private Timestamp isDeleted;
}
