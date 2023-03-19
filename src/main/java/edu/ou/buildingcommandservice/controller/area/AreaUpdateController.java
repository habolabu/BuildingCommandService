package edu.ou.buildingcommandservice.controller.area;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.area.AreaUpdateRequest;
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
@RequestMapping(EndPoint.Area.BASE)
public class AreaUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> areaUpdateService;

    /**
     * Update exist area
     *
     * @param areaUpdateRequest new information of exist area
     * @return id of area
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_AREA)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistArea(
            @Validated
            @RequestBody
            AreaUpdateRequest areaUpdateRequest
    ) {
        return new ResponseEntity<>(
                areaUpdateService.execute(areaUpdateRequest),
                HttpStatus.OK
        );
    }

}
