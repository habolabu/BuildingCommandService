package edu.ou.buildingcommandservice.controller.priceTag;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.priceTag.PriceTagAddRequest;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.PriceTag.BASE)
public class PriceTagAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> priceTagAddService;

    /**
     * Add new price tag.
     *
     * @param priceTagAddRequest information about new price tag
     * @return id of new price tag
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_PRICE_TAG)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewPriceTag(
            @Validated
            @RequestBody
            PriceTagAddRequest priceTagAddRequest
    ) {
        return new ResponseEntity<>(
                priceTagAddService.execute(priceTagAddRequest),
                HttpStatus.OK
        );
    }
}
