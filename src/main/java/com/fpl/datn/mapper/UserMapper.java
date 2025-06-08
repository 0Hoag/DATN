package com.fpl.datn.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.fpl.datn.dto.request.RegisterRequest;
import com.fpl.datn.dto.request.UpdateProfileRequest;
import com.fpl.datn.dto.request.UpdateUserRequest;
import com.fpl.datn.dto.request.UserRequest;
import com.fpl.datn.dto.response.UserResponse;
import com.fpl.datn.models.Permission;
import com.fpl.datn.models.Role;
import com.fpl.datn.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserRegister(RegisterRequest request);

    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UpdateUserRequest request);

    void updateProfile(@MappingTarget User user, UpdateProfileRequest request);

    default Set<Role> mapRole(Set<String> roleNames) {
        if (roleNames == null) {
            return null;
        }
        return roleNames.stream()
                .map(name -> {
                    Role role = new Role();
                    role.setName(name);
                    return role;
                })
                .collect(Collectors.toSet());
    }

    default Set<String> mapRolePermission(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream().map(Permission::getName).collect(Collectors.toSet());
    }
}
