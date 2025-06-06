package com.fpl.datn.dto.request;

import com.fpl.datn.models.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
    String fullName;
    String email;
    String password;
    String phone;
}
