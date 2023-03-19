package edu.ou.buildingcommandservice.common.mapper;

import edu.ou.buildingcommandservice.data.entity.PriceTagEntity;
import edu.ou.buildingcommandservice.data.pojo.request.priceTag.PriceTagAddRequest;
import edu.ou.buildingcommandservice.data.pojo.request.priceTag.PriceTagUpdateRequest;
import edu.ou.coreservice.common.util.SlugUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PriceTagEntityMapper {
    PriceTagEntityMapper INSTANCE = Mappers.getMapper(PriceTagEntityMapper.class);

    /**
     * Map PriceTagAddRequest object to PriceTagEntity object
     *
     * @param priceTagRequest represents for PriceTagAddRequest object
     * @return PriceTagEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    PriceTagEntity fromPriceTagAddRequest(PriceTagAddRequest priceTagRequest);

    /**
     * Map PriceTagUpdateRequest object to PriceTagEntity object
     *
     * @param priceTagUpdateRequest represents for PriceTagUpdateRequest object
     * @return PriceTagEntity object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "slug", source = "name", qualifiedByName = "nameToSlug")
    @Mapping(target = "isDeleted", ignore = true)
    PriceTagEntity fromPriceTagUpdateRequest(PriceTagUpdateRequest priceTagUpdateRequest);

    /**
     * Convert price tag name to price tag slug
     *
     * @param name name of price tag
     * @return slug of price tag
     * @author Nguyen Trung Kien - OU
     */
    @Named("nameToSlug")
    static String toSlug(String name) {
        return SlugUtils.createSlug(name);
    }
}
