package com.fpl.datn.controller;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.request.PermissionRequest;
import com.fpl.datn.dto.response.PermissionResponse;
import com.fpl.datn.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .code(1000)
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping("/{name}")
    ApiResponse<PermissionResponse> detail(@PathVariable String name) {
        return ApiResponse.<PermissionResponse>builder()
                .code(1000)
                .result(permissionService.getPermission(name))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
//        var authenticated = SecurityContextHolder.getContext().getAuthentication();
//        log.info("Username: {}", authenticated.getName());
//        authenticated.getAuthorities().stream().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(1000)
                .result(permissionService.getALL())
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name) {
        permissionService.delete(name);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("permission has been delete")
                .build();
    }
}
