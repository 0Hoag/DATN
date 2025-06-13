package com.fpl.datn.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.request.AddressRequest;
import com.fpl.datn.dto.response.AddressResponse;
import com.fpl.datn.service.AddressService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    @GetMapping("/{userId}")
    public ApiResponse<List<AddressResponse>> getAddressByUserId(@PathVariable int userId) {
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressService.findByUserId(userId))
                .build();
    }

    @PostMapping("/{userId}")
    public ApiResponse<AddressResponse> createByUserId(@PathVariable int userId, @RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.createByUserId(userId, request))
                .build();
    }
}
