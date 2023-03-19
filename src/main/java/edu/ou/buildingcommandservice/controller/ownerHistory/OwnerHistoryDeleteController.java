package edu.ou.buildingcommandservice.controller.ownerHistory;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryDeleteRequest;
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
@RequestMapping(EndPoint.OwnerHistory.BASE)
public class OwnerHistoryDeleteController {
    private final IBaseService<IBaseRequest, IBaseResponse> ownerHistoryDeleteService;

    /**
     * Delete exist owner history
     *
     * @param ownerHistoryDeleteRequest id of owner history which want to delete
     * @return id of owner history
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.DELETE_EXIST_OWNER_HISTORY)
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> deleteExistOwnerHistory(
            @Validated
            @RequestBody
            OwnerHistoryDeleteRequest ownerHistoryDeleteRequest
    ) {
        return new ResponseEntity<>(
                ownerHistoryDeleteService.execute(ownerHistoryDeleteRequest),
                HttpStatus.OK
        );
    }
}
