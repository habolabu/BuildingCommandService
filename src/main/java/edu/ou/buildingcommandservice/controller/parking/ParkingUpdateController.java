package edu.ou.buildingcommandservice.controller.parking;


import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.parking.ParkingUpdateRequest;
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
@RequestMapping(EndPoint.Parking.BASE)
public class ParkingUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> parkingUpdateService;

    /**
     * Update exist parking
     *
     * @param parkingUpdateRequest new information of exist parking
     * @return id of parking
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_PARKING)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistParking(
            @Validated
            @RequestBody
            ParkingUpdateRequest parkingUpdateRequest
    ) {
        return new ResponseEntity<>(
                parkingUpdateService.execute(parkingUpdateRequest),
                HttpStatus.OK
        );
    }
}
