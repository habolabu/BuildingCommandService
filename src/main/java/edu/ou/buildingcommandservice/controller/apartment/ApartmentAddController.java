package edu.ou.buildingcommandservice.controller.apartment;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.apartment.ApartmentAddRequest;
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
@RequestMapping(EndPoint.Apartment.BASE)
public class ApartmentAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> apartmentAddService;

    /**
     * Add new apartment
     *
     * @param apartmentAddRequest new apartment information
     * @return id of new apartment
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_APARTMENT)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewApartment(
            @Validated
            @RequestBody
            ApartmentAddRequest apartmentAddRequest
    ) {
        return new ResponseEntity<>(
                apartmentAddService.execute(apartmentAddRequest),
                HttpStatus.OK
        );
    }
}
