package com.fpl.datn.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
	String email;
    String oldPassword;
    String newPassword;
    String confirmNewPassword;
}
