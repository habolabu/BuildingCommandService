package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.OwnerHistoryEntity;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.ownerHistory.OwnerHistoryUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OwnerHistoryEntityMapper {
    OwnerHistoryEntityMapper INSTANCE = Mappers.getMapper(OwnerHistoryEntityMapper.class);

    /**
     * Map OwnerHistoryAddRequest object to OwnerHistoryEntity object
     *
     * @param ownerHistoryAddRequest represents for OwnerHistoryAddRequest object
     * @return OwnerHistoryEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    OwnerHistoryEntity fromOwnerHistoryAddRequest(OwnerHistoryAddRequest ownerHistoryAddRequest);

    /**
     * Map OwnerHistoryUpdateRequest object to OwnerHistoryEntity object
     *
     * @param ownerHistoryUpdateRequest represents for OwnerHistoryUpdateRequest object
     * @return OwnerHistoryEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    OwnerHistoryEntity fromOwnerHistoryUpdateRequest(OwnerHistoryUpdateRequest ownerHistoryUpdateRequest);

}
