package edu.ou.buildingcommandservice.data.pojo.request.priceTag;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PriceTagDeleteRequest implements IBaseRequest {
    @NotBlank
    private String slug;
}
