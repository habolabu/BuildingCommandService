package edu.ou.buildingcommandservice.data.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class OwnerHistoryEntityPK implements Serializable {
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
