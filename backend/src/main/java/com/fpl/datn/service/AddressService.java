package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.request.AddressRequest;
import com.fpl.datn.dto.response.AddressResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.AddressMapper;
import com.fpl.datn.models.Address;
import com.fpl.datn.repository.AddressRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository repository;
    UserRepository userRepository;
    AddressMapper mapper;

    public List<AddressResponse> findByUserId(int userId) {
        var userAddress = repository.findByIsDeleteFalseAndUser_Id(userId);
        if (userAddress == null || userAddress.isEmpty()) {
            return null;
        }
        return userAddress.stream().map(mapper::toAddressResoonse).toList();
    }

    public AddressResponse createByUserId(int userId, AddressRequest request) {

        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var userAddresses = repository.findByIsDeleteFalseAndUser_Id(userId);

        boolean isFirstAddress = userAddresses.isEmpty();
        boolean shouldBeDefault = isFirstAddress || Boolean.TRUE.equals(request.getIsDefault());
        if (shouldBeDefault) {
            for (var addr : userAddresses) {
                if (Boolean.TRUE.equals(addr.getIsDefault())) {
                    addr.setIsDefault(false);
                    repository.save(addr);
                }
            }
        }
        var address = mapper.toAddress(request);
        address.setUser(user);
        address.setIsDefault(shouldBeDefault);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        address.setIsDelete(false);
        repository.save(address);
        return mapper.toAddressResoonse(address);
    }

    public AddressResponse updateByUserId(int userId, AddressRequest request) {
        var userAddresses = repository.findByIsDeleteFalseAndUser_Id(userId);
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

    public void delete(int addressId) {
        Address address =
                repository.findById(addressId).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        if (Boolean.TRUE.equals(address.getIsDelete())) throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        if (address.getOrders() == null || address.getOrders().isEmpty()) {
            repository.delete(address);
        } else {
            address.setIsDelete(true);
            repository.save(address);
        }

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            List<Address> remainingAddresses =
                    repository.findByIsDeleteFalseAndUser_Id(address.getUser().getId());

            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                repository.save(newDefault);
            }
        }
    }
}
