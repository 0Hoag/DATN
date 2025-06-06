package com.fpl.datn.mapper;

import com.fpl.datn.dto.request.PermissionRequest;
import com.fpl.datn.dto.response.PermissionResponse;
import com.fpl.datn.models.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
