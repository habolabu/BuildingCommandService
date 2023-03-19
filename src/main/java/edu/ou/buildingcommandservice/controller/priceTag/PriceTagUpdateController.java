package edu.ou.buildingcommandservice.controller.priceTag;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.priceTag.PriceTagUpdateRequest;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.PriceTag.BASE)
public class PriceTagUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> priceTagUpdateService;

    /**
     * Update exist price tag
     *
     * @param priceTagUpdateRequest new information of exist price tag
     * @return id of price tag
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_PRICE_TAG)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistPriceTag(
            @Validated
            @RequestBody
            PriceTagUpdateRequest priceTagUpdateRequest
    ) {
        return new ResponseEntity<>(
                priceTagUpdateService.execute(priceTagUpdateRequest),
                HttpStatus.OK
        );
    }
}
