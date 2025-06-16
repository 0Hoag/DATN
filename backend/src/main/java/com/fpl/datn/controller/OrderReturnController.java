package com.fpl.datn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.OrderReturnRequest;
import com.fpl.datn.dto.request.OrderReturnStatusRequest;
import com.fpl.datn.dto.response.OrderReturnResponse;
import com.fpl.datn.service.OrderReturnService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/order-return")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderReturnController {
    OrderReturnService orderReturnService;

    @GetMapping
    ApiResponse<PageResponse<OrderReturnResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean sort) {
        return ApiResponse.<PageResponse<OrderReturnResponse>>builder()
                .result(orderReturnService.get(page, size, sort))
                .build();
    }

    @PostMapping
    ApiResponse<OrderReturnResponse> create(@RequestBody OrderReturnRequest request) {
        return ApiResponse.<OrderReturnResponse>builder()
                .result(orderReturnService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<OrderReturnResponse> update(@PathVariable int id, @RequestBody OrderReturnStatusRequest request) {
        return ApiResponse.<OrderReturnResponse>builder()
                .result(orderReturnService.updateStatus(id, request))
                .build();
    }
}
