package edu.ou.buildingcommandservice.common.mapper;


import edu.ou.buildingcommandservice.data.entity.AreaEntity;
import edu.ou.buildingcommandservice.data.pojo.request.area.AreaInsertionRequest;
import edu.ou.buildingcommandservice.data.pojo.request.area.AreaUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AreaEntityMapper {
    AreaEntityMapper INSTANCE = Mappers.getMapper(AreaEntityMapper.class);

    /**
     * Map AreaInsertionRequest object to AreaEntity object
     *
     * @param areaInsertionRequest represents for AreaInsertionRequest object
     * @return AreaEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    AreaEntity fromAreaInsertionRequest(AreaInsertionRequest areaInsertionRequest);

    /**
     * Map AreaUpdateRequest object to AreaEntity object
     *
     * @param areaUpdateRequest represents for AreaUpdateRequest object
     * @return AreaEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    AreaEntity fromAreaUpdateRequest(AreaUpdateRequest areaUpdateRequest);

    /**
     * Convert area name to area slug
     *
     * @param name name of area
     * @return slug of area
     * @author Nguyen Trung Kien - OU
     */
    @Named("nameToSlug")
    static String toSlug(String name) {
        return SlugUtils.createSlug(name);
    }
}
