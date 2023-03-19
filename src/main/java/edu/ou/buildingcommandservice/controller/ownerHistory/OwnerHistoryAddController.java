package edu.ou.buildingcommandservice.controller.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryAddRequest;
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
@RequestMapping(EndPoint.OwnerHistory.BASE)
public class OwnerHistoryAddController {
    private final IBaseService<IBaseRequest, IBaseResponse> ownerHistoryAddService;

    /**
     * Add new owner history
     *
     * @param ownerHistoryAddRequest new owner history information
     * @return id of new owner history
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.ADD_NEW_OWNER_HISTORY)
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> addNewOwnerHistory(
            @Validated
            @RequestBody
            OwnerHistoryAddRequest ownerHistoryAddRequest
    ) {
        return new ResponseEntity<>(
                ownerHistoryAddService.execute(ownerHistoryAddRequest),
                HttpStatus.OK
        );
    }
}
