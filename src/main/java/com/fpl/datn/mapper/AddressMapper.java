package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.request.AddressRequest;
import com.fpl.datn.dto.response.AddressResponse;
import com.fpl.datn.models.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponse toAddressResoonse(Address address);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Address toAddress(AddressRequest request);
}
