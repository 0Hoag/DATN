package com.fpl.datn.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.fpl.datn.dto.request.OrderRequest;
import com.fpl.datn.dto.request.OrderStatusRequest;
import com.fpl.datn.dto.request.UpdateOrderRequest;
import com.fpl.datn.dto.response.OrderResponse;
import com.fpl.datn.service.OrderService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @GetMapping
    ApiResponse<PageResponse<OrderResponse>> getAll(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean sort) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .result(orderService.getAll(page, size, sort))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<OrderResponse> getOrder(@PathVariable int id) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(id))
                .build();
    }

    @GetMapping("/search")
    ApiResponse<PageResponse<OrderResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "true") boolean sort) {
        return ApiResponse.<PageResponse<OrderResponse>>builder()
                .result(orderService.search(
                        keyword, id, phone, orderStatus, paymentStatus, startDate, endDate, page, size, sort))
                .build();
    }

    @PostMapping
    ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<OrderResponse> updateOrder(@PathVariable int id, @RequestBody UpdateOrderRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.update(id, request))
                .build();
    }

    @PutMapping("/status/{id}")
    ApiResponse<OrderResponse> updateOrderStatus(@PathVariable int id, @RequestBody OrderStatusRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrderStatus(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<OrderResponse> delteOrder(@PathVariable int id) {
        orderService.delete(id);
        return ApiResponse.<OrderResponse>builder().message("Delete Success!").build();
    }
}
