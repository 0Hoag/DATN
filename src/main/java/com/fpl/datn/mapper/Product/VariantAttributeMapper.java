package com.fpl.datn.mapper.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.fpl.datn.dto.request.Product.UpdateVariantAttributeRequest;
import com.fpl.datn.dto.request.Product.VariantAttributeRequest;
import com.fpl.datn.dto.response.Product.VariantAttributeResponse;
import com.fpl.datn.models.VariantAttribute;
import com.fpl.datn.models.VariantAttributeValue;

@Mapper(
        componentModel = "spring",
        uses = {VariantAttributeValueMapper.class})
public interface VariantAttributeMapper {

    // Entity -> Response
    @Mapping(source = "values", target = "values") // MapStruct sẽ dùng VariantAttributeValueMapper
    VariantAttributeResponse toResponse(VariantAttribute attribute);

    // Request -> Entity (Create)
    @Mapping(target = "values", expression = "java(mapToValueEntities(request.getValues()))")
    VariantAttribute toEntity(VariantAttributeRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "values", ignore = true)
    void update(@MappingTarget VariantAttribute attribute, UpdateVariantAttributeRequest request);

    // Helper
    @Named("mapToValueEntities")
    default List<VariantAttributeValue> mapToValueEntities(List<String> valueStrings) {
        if (valueStrings == null) return null;
        return valueStrings.stream()
                .map(val -> {
                    VariantAttributeValue entity = new VariantAttributeValue();
                    entity.setValue(val);
                    return entity;
                })
                .collect(Collectors.toList());
    }
}