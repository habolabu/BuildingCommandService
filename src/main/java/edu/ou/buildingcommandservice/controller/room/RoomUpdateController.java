package edu.ou.buildingcommandservice.controller.room;

import edu.ou.buildingcommandservice.common.constant.EndPoint;
import edu.ou.buildingcommandservice.data.pojo.request.room.RoomUpdateRequest;
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
@RequestMapping(EndPoint.Room.BASE)
public class RoomUpdateController {
    private final IBaseService<IBaseRequest, IBaseResponse> roomUpdateService;

    /**
     * Update exist room
     *
     * @param roomUpdateRequest new information of exist room
     * @return id of room
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.MODIFY_EXIST_ROOM)
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> updateExistRoom(
            @Validated
            @RequestBody
            RoomUpdateRequest roomUpdateRequest
    ) {
        return new ResponseEntity<>(
                roomUpdateService.execute(roomUpdateRequest),
                HttpStatus.OK
        );
    }
}
