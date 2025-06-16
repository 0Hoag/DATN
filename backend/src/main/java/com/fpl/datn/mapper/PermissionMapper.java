package com.fpl.datn.mapper;

import org.mapstruct.Mapper;

import com.fpl.datn.dto.request.PermissionRequest;
import com.fpl.datn.dto.response.PermissionResponse;
import com.fpl.datn.models.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
