package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.ApartmentEntity;
import edu.ou.buildingcommandservice.data.pojo.request.apartment.ApartmentAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.apartment.ApartmentUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApartmentEntityMapper {
    ApartmentEntityMapper INSTANCE = Mappers.getMapper(ApartmentEntityMapper.class);

    /**
     * Map ApartmentAddRequest object to ApartmentEntity object
     *
     * @param apartmentAddRequest represents for ApartmentAddRequest object
     * @return ApartmentEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    ApartmentEntity fromApartmentAddRequest(ApartmentAddRequest apartmentAddRequest);

    /**
     * Map ApartmentUpdateRequest object to ApartmentEntity object
     *
     * @param apartmentUpdateRequest represents for ApartmentUpdateRequest object
     * @return ApartmentEntity object
     * @author Nguyen Trung Kien - OU
     */

    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    ApartmentEntity fromApartmentUpdateRequest(ApartmentUpdateRequest apartmentUpdateRequest);

    /**
     * Convert apartment name to apartment slug
     *
     * @param name name of apartment
     * @return slug of apartment
     * @author Nguyen Trung Kien - OU
     */
    @Named("nameToSlug")
    static String toSlug(String name) {
        return SlugUtils.createSlug(name);
    }

}
