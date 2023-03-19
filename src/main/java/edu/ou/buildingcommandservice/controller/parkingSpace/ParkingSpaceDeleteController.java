package edu.ou.buildingcommandservice.controller.parkingSpace;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.parkingSpace.ParkingSpaceDeleteRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.ParkingSpace.BASE)
public class ParkingSpaceDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingSpaceDeleteService;

    /**
     * Delete exist parking space
     *
     * @param parkingSpaceDeleteRequest id of parking space which want to delete
     * @return id of parking space
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_PARKING_SPACE)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistParkingSpace(
            @Validated
            @RequestBody
            ParkingSpaceDeleteRequest parkingSpaceDeleteRequest
    ) {
        return new ResponseEntity<>(
                parkingSpaceDeleteService.execute(parkingSpaceDeleteRequest),
                HttpStatus.OK
        );
    }
}
