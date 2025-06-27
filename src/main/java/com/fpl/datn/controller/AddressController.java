package com.fpl.datn.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.request.Product.AddressRequest;
import com.fpl.datn.dto.response.Product.AddressResponse;
import com.fpl.datn.service.AddressService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    @PostMapping
    ApiResponse<AddressResponse> create(@RequestBody AddressRequest request) {
        addressService.create(request);
        return ApiResponse.<AddressResponse>builder()
                .code(1000)
                .message("Address has been create")
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<AddressResponse> detail(@PathVariable Integer id) {
        return ApiResponse.<AddressResponse>builder()
                .code(1000)
                .result(addressService.detail(id))
                .build();
    }

    @GetMapping
    ApiResponse<List<AddressResponse>> getAll() {
        return ApiResponse.<List<AddressResponse>>builder()
                .code(1000)
                .result(addressService.all())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> delete(@PathVariable Integer id) {
        addressService.delete(id);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Address has been delete")
                .build();
    }
}
