package edu.ou.buildingcommandservice.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(
        name = "OwnerHistory",
        schema = "BuildingCommandService"
)
public class OwnerHistoryEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "joinDate")
    private Timestamp joinDate = new Timestamp(System.currentTimeMillis());

    @Basic
    @JsonIgnore
    @Column(name = "isDeleted")
    private Timestamp isDeleted;

    @Basic
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "ownerId")
    private int ownerId;

    @Basic
    @Min(
            value = 1,
            message = "The value must be greater or equals than 1"
    )
    @Column(name = "roomId")
    private int roomId;
}
