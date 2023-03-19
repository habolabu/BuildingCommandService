package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.ParkingTypeEntity;
import edu.ou.buildingcommandservice.data.pojo.request.parkingType.ParkingTypeAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.parkingType.ParkingTypeUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParkingTypeEntityMapper {
    ParkingTypeEntityMapper INSTANCE = Mappers.getMapper(ParkingTypeEntityMapper.class);

    /**
     * Map ParkingTypeAddRequest object to ParkingTypeEntity object
     *
     * @param parkingTypeAddRequest represents for ParkingTypeAddRequest object
     * @return ParkingTypeEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    ParkingTypeEntity fromParkingTypeAddRequest(ParkingTypeAddRequest parkingTypeAddRequest);

    /**
     * Map ParkingTypeUpdateRequest object to ParkingTypeEntity object
     *
     * @param parkingTypeUpdateRequest represents for ParkingTypeUpdateRequest object
     * @return ParkingTypeEntity object
     * @author Nguyen Trung Kien - OU
     */

    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    ParkingTypeEntity fromParkingTypeUpdateRequest(ParkingTypeUpdateRequest parkingTypeUpdateRequest);

    /**
     * Convert parking type name to parking type slug
     *
     * @param name name of parking type
     * @return slug of parking type
     * @author Nguyen Trung Kien - OU
     */
    @Named("nameToSlug")
    static String toSlug(String name) {
        return SlugUtils.createSlug(name);
    }
}
