package edu.ou.buildingcommandservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "PriceTag",
        schema = "BuildingCommandService"
)
public class PriceTagEntity implements Serializable {
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
    @NotNull
    @Min(
            value = 1000,
            message = "The value must greater or equals than 1000"
    )
    @Column(name = "pricePerDay")
    private BigDecimal pricePerDay;

    @Basic
    @Column(name = "isDeleted")
    @JsonIgnore
    private Timestamp isDeleted;
}
