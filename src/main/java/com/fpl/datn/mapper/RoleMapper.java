package com.fpl.datn.mapper;

import com.fpl.datn.dto.request.RoleRequest;
import com.fpl.datn.dto.response.RoleResponse;
import com.fpl.datn.models.Permission;
import com.fpl.datn.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    default Set<String> map(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
