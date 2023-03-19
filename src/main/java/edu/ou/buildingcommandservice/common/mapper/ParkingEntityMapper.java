package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.ParkingEntity;
import edu.ou.buildingcommandservice.data.pojo.request.parking.ParkingAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.parking.ParkingUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParkingEntityMapper {
    ParkingEntityMapper INSTANCE = Mappers.getMapper(ParkingEntityMapper.class);

    /**
     * Map ParkingAddRequest object to ParkingEntity object
     *
     * @param parkingAddRequest represents for ParkingAddRequest object
     * @return ParkingEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    ParkingEntity fromParkingAddRequest(ParkingAddRequest parkingAddRequest);

    /**
     * Map ParkingUpdateRequest object to ParkingEntity object
     *
     * @param parkingUpdateRequest represents for ParkingUpdateRequest object
     * @return ParkingEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    ParkingEntity fromParkingUpdateRequest(ParkingUpdateRequest parkingUpdateRequest);

    /**
     * Convert room name to parking slug
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
