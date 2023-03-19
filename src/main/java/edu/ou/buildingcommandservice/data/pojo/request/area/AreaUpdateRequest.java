package edu.ou.buildingcommandservice.data.pojo.request.area;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AreaUpdateRequest implements IBaseRequest {
    private int id;

    @NotBlank
    @Size(
            min = 1,
            max = 50
    )
    private String name;

    @NotBlank
    @Size(max = 255)
    private String address;
}
