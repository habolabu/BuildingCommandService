package edu.ou.buildingcommandservice.controller.area;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.area.AreaInsertionRequest;
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
@RequestMapping(EndPoint.Area.BASE)
public class AreaAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> areaInsertionService;

    /**
     * Add new area.
     *
     * @param areaInsertionRequest information about new area
     * @return id of new area
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_AREA)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewArea(
            @Validated
            @RequestBody
            AreaInsertionRequest areaInsertionRequest
    ) {
        return new ResponseEntity<>(
                areaInsertionService.execute(areaInsertionRequest),
                HttpStatus.OK
        );
    }

}
