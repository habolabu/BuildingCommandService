package edu.ou.buildingcommandservice.controller.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryUpdateRequest;
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
@RequestMapping(EndPoint.OwnerHistory.BASE)
public class OwnerHistoryUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> ownerHistoryUpdateService;

    /**
     * Update exist owner history
     *
     * @param ownerHistoryUpdateRequest new information of exist owner history
     * @return id of owner history
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_OWNER_HISTORY)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistOwnerHistory(
            @Validated
            @RequestBody
            OwnerHistoryUpdateRequest ownerHistoryUpdateRequest
    ) {
        return new ResponseEntity<>(
                ownerHistoryUpdateService.execute(ownerHistoryUpdateRequest),
                HttpStatus.OK
        );
    }
}
