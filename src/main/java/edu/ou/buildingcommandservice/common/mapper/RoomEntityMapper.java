package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.RoomEntity;
import edu.ou.buildingcommandservice.data.pojo.request.room.RoomAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.room.RoomUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomEntityMapper {
    RoomEntityMapper INSTANCE = Mappers.getMapper(RoomEntityMapper.class);

    /**
     * Map RoomAddRequest object to RoomEntity object
     *
     * @param roomAddRequest represents for RoomAddRequest object
     * @return RoomEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    RoomEntity fromRoomAddRequest(RoomAddRequest roomAddRequest);

    /**
     * Map RoomUpdateRequest object to RoomEntity object
     *
     * @param roomUpdateRequest represents for RoomUpdateRequest object
     * @return RoomEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    RoomEntity fromRoomUpdateRequest(RoomUpdateRequest roomUpdateRequest);

    /**
     * Convert room name to room slug
     *
     * @param name name of room
     * @return slug of room
     * @author Nguyen Trung Kien - OU
     */
    @Named("nameToSlug")
    static String toSlug(String name) {
        return SlugUtils.createSlug(name);
    }
}
