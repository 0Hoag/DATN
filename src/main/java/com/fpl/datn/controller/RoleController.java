package com.fpl.datn.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.request.RoleRequest;
import com.fpl.datn.dto.response.RoleResponse;
import com.fpl.datn.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .code(1000)
                .result(roleService.create(request))
                .build();
    }

    @GetMapping("/{name}")
    ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .code(1000)
                .result(roleService.getRole(name))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(1000)
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name) {
        roleService.delete(name);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("role has been delete")
                .build();
    }
}
