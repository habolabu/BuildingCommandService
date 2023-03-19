package edu.ou.buildingcommandservice.controller.parkingType;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.parkingType.ParkingTypeAddRequest;
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
@RequestMapping(EndPoint.ParkingType.BASE)
public class ParkingTypeAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingTypeAddService;

    /**
     * Add new parking type
     *
     * @param parkingTypeAddRequest new apartment information
     * @return id of new parking type
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_PARKING_TYPE)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewParkingType(
            @Validated
            @RequestBody
            ParkingTypeAddRequest parkingTypeAddRequest
    ) {
        return new ResponseEntity<>(
                parkingTypeAddService.execute(parkingTypeAddRequest),
                HttpStatus.OK
        );
    }
}
