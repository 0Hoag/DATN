package com.fpl.datn.mapper;

import com.fpl.datn.dto.request.Product.AddressRequest;
import com.fpl.datn.dto.request.Product.UpdateAddressRequest;
import com.fpl.datn.dto.response.Product.AddressResponse;
import com.fpl.datn.models.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest request);

    AddressResponse toAddressResponse(Address entity);

    void update(@MappingTarget Address entity, UpdateAddressRequest request);
}
