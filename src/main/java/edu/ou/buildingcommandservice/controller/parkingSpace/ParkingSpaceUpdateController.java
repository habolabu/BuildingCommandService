package edu.ou.buildingcommandservice.controller.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.parkingSpace.ParkingSpaceUpdateRequest;
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
@RequestMapping(EndPoint.ParkingSpace.BASE)
public class ParkingSpaceUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingSpaceUpdateService;

    /**
     * Update exist parking space
     *
     * @param parkingSpaceUpdateRequest new information of exist parking space
     * @return id of parking space
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_PARKING_SPACE)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistParkingSpace(
            @Validated
            @RequestBody
            ParkingSpaceUpdateRequest parkingSpaceUpdateRequest
    ) {
        return new ResponseEntity<>(
                parkingSpaceUpdateService.execute(parkingSpaceUpdateRequest),
                HttpStatus.OK
        );
    }
}
