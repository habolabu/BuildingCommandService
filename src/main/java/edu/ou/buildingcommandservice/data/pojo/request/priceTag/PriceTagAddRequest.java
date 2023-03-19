package edu.ou.buildingcommandservice.data.pojo.request.priceTag;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class PriceTagAddRequest implements IBaseRequest {
    @NotBlank
    @Size(
            min = 1,
            max = 50
    )
    private String name;

    @NotNull
    @Min(
            value = 1000,
            message = "The value must greater or equals than 1000"
    )
    private BigDecimal pricePerDay;
}
