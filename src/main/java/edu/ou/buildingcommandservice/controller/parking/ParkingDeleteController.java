package edu.ou.buildingcommandservice.controller.parking;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.parking.ParkingDeleteRequest;
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
@RequestMapping(EndPoint.Parking.BASE)
public class ParkingDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingDeleteService;

    /**
     * Delete exist parking
     *
     * @param parkingDeleteRequest id of parking which want to delete
     * @return slug of parking
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_PARKING)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistParking(
            @Validated
            @RequestBody
            ParkingDeleteRequest parkingDeleteRequest
    ) {
        return new ResponseEntity<>(
                parkingDeleteService.execute(parkingDeleteRequest),
                HttpStatus.OK
        );
    }
}
