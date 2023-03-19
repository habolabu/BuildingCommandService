package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntity;
import edu.ou.buildingcommandservice.data.entity.ParkingSpaceEntityPK;
import edu.ou.buildingcommandservice.data.pojo.request.parkingSpace.ParkingSpaceAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.parkingSpace.ParkingSpaceUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParkingSpaceEntityMapper {
    ParkingSpaceEntityMapper INSTANCE = Mappers.getMapper(ParkingSpaceEntityMapper.class);

    /**
     * Map ParkingSpaceAddRequest object to ParkingSpaceEntity object
     *
     * @param parkingSpaceAddRequest represents for ParkingSpaceAddRequest object
     * @return ParkingSpaceEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "parkingId", source = "referenceKey", qualifiedByName = "referenceKeyToParkingId")
    @Mapping(target = "parkingTypeId", source = "referenceKey", qualifiedByName = "referenceKeyToParkingTypeId")
    @Mapping(target = "availableSpace", ignore = true)
    ParkingSpaceEntity fromParkingSpaceAddRequest(ParkingSpaceAddRequest parkingSpaceAddRequest);

    /**
     * Map ParkingSpaceUpdateRequest object to ParkingSpaceEntity object
     *
     * @param parkingSpaceUpdateRequest represents for ParkingSpaceUpdateRequest object
     * @return ParkingSpaceEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "parkingId", source = "referenceKey", qualifiedByName = "referenceKeyToParkingId")
    @Mapping(target = "parkingTypeId", source = "referenceKey", qualifiedByName = "referenceKeyToParkingTypeId")
    ParkingSpaceEntity fromParkingSpaceUpdateRequest(ParkingSpaceUpdateRequest parkingSpaceUpdateRequest);

    /**
     * Convert reference key to parking space
     *
     * @param parkingSpaceEntityPK key of parking space
     * @return parking id of parking space
     * @author Nguyen Trung Kien - OU
     */
    @Named("referenceKeyToParkingId")
    static int toParkingId(ParkingSpaceEntityPK parkingSpaceEntityPK) {
        return parkingSpaceEntityPK.getParkingId();
    }

    /**
     * Convert reference key to parking space
     *
     * @param parkingSpaceEntityPK key of parking space
     * @return parking id of parking space
     * @author Nguyen Trung Kien - OU
     */
    @Named("referenceKeyToParkingTypeId")
    static int toParkingTypeId(ParkingSpaceEntityPK parkingSpaceEntityPK) {
        return parkingSpaceEntityPK.getParkingTypeId();
    }
}
