package com.fpl.datn.service;

import com.fpl.datn.dto.request.Product.AddressRequest;
import com.fpl.datn.dto.request.Product.UpdateAddressRequest;
import com.fpl.datn.dto.response.Product.AddressResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.AddressMapper;
import com.fpl.datn.models.Address;
import com.fpl.datn.repository.AddressRepository;
import com.fpl.datn.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    UserRepository userRepository;

    public boolean create(AddressRequest request) {
        try {
            if (userRepository.existsById(request.getUserID())) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }

            var address = addressMapper.toAddress(request);
            addressRepository.save(address);

            return true;
        } catch (AppException e) {
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }
    }

    public AddressResponse detail(int id) {
        return addressMapper.toAddressResponse(addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND)));
    }

    public List<AddressResponse> all() {
        return addressRepository.findAll()
                .stream().map(addressMapper::toAddressResponse)
                .collect(Collectors.toList());
    }

    public boolean delete(int id) {
        try {
            addressRepository.deleteById(id);
            return true;
        } catch (AppException e) {
            throw new AppException(ErrorCode.UNCATEGORIZE_EXCEPTION);
        }
    }

    public void update(int id, UpdateAddressRequest request)
    {
        if (userRepository.existsById(request.getUserID())) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        var address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        addressMapper.update(address, request);
    }
}
