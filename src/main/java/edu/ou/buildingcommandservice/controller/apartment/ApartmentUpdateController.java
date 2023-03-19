package edu.ou.buildingcommandservice.controller.apartment;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.apartment.ApartmentUpdateRequest;
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
@RequestMapping(EndPoint.Apartment.BASE)
public class ApartmentUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> apartmentUpdateService;

    /**
     * Update exist apartment
     *
     * @param apartmentUpdateRequest new information of exist apartment
     * @return id of apartment
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_APARTMENT)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistApartment(
            @Validated
            @RequestBody
            ApartmentUpdateRequest apartmentUpdateRequest
    ) {
        return new ResponseEntity<>(
                apartmentUpdateService.execute(apartmentUpdateRequest),
                HttpStatus.OK
        );
    }
}
