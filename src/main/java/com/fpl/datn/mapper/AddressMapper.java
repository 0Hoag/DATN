package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.fpl.datn.dto.request.Product.AddressRequest;
import com.fpl.datn.dto.request.Product.UpdateAddressRequest;
import com.fpl.datn.dto.response.Product.AddressResponse;
import com.fpl.datn.models.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest request);

    AddressResponse toAddressResponse(Address entity);

    void update(@MappingTarget Address entity, UpdateAddressRequest request);
}
