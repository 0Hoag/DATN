package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.request.AddressRequest;
import com.fpl.datn.dto.response.AddressResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.AddressMapper;
import com.fpl.datn.repository.AddressRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository repository;
    AddressMapper mapper;

    public List<AddressResponse> findByUserId(int userId) {
        var userAddress = repository.findByUser_Id(userId);
        if (userAddress == null || userAddress.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return userAddress.stream().map(mapper::toAddressResoonse).toList();
    }

    public AddressResponse createByUserId(int userId, AddressRequest request) {
        var userAddresses = repository.findByUser_Id(userId);
        if (userAddresses == null || userAddresses.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            for (var addr : userAddresses) {
                if (Boolean.TRUE.equals(addr.getIsDefault())) {
                    addr.setIsDefault(false);
                    repository.save(addr); // cập nhật lại vào DB
                }
            }
        }
        var address = mapper.toAddress(request);
        address.setCreatedAt(LocalDateTime.now());
        address.setUser(userAddresses.get(0).getUser());
        address.setIsDefault(request.getIsDefault());
        repository.save(address);
        return mapper.toAddressResoonse(address);
    }

    public AddressResponse updateByUserId(int userId, AddressRequest request) {
        var userAddresses = repository.findByUser_Id(userId);
        if (userAddresses == null || userAddresses.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            for (var addr : userAddresses) {
                if (Boolean.TRUE.equals(addr.getIsDefault())) {
                    addr.setIsDefault(false);
                    repository.save(addr);
                }
            }
        }
        var address = mapper.toAddress(request);
        address.setCreatedAt(LocalDateTime.now());
        address.setUser(userAddresses.get(0).getUser());
        address.setIsDefault(request.getIsDefault());
        repository.save(address);
        return mapper.toAddressResoonse(address);
    }
}
