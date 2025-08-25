package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fpl.datn.constant.PredefinedRole;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.*;
import com.fpl.datn.dto.response.UserResponse;
import com.fpl.datn.enums.ActionActicityLog;
import com.fpl.datn.enums.ActionActicityModule;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.UserMapper;
import com.fpl.datn.models.Role;
import com.fpl.datn.models.User;
import com.fpl.datn.repository.RoleRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    JavaMailSender javaMailSender;
    UserRepository userRepositories;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    ActivitylogService activitylogService;

    public void sendSimpleMessage(MailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getTo());
        message.setFrom("phonezone.shop11@gmail.com");
        message.setSubject(request.getSubject());
        message.setText(request.getText());

        javaMailSender.send(message);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('MANAGE_USERS')")
    public Boolean Create(UserRequest request) {
        Set<Role> roles = roleRepository
                .findAllByNameIn(request.getRoles())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        if (userRepositories.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepositories.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepositories.save(user);

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Create)
                .description("Tạo người dùng: " + user.getFullName())
                .module(ActionActicityModule.User)
                .objectID(user.getId())
                .build());

        return true;
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public UserResponse Update(int id, UpdateUserRequest request) {
        var user = userRepositories
                .findByIdAndNotDeleted(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = roleRepository
                    .findAllByNameIn(request.getRoles())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            user.setRoles(roles);
        }

        user.setUpdatedAt(LocalDateTime.now());

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Update)
                .description("Cập nhập người dùng: " + user.getFullName())
                .module(ActionActicityModule.User)
                .objectID(user.getId())
                .build());

        return userMapper.toUserResponse(userRepositories.save(user));
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public UserResponse Detail(int id) {
        User user = userRepositories
                .findByIdAndNotDeleted(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void DeleteSoftOne(int id, DeleteRequest request) {
        User user = userRepositories.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getDeletedAt() != null) {
            throw new AppException(ErrorCode.USER_ALREADY_DELETED);
        }

        if (request.getReason() == "") {
            throw new AppException(ErrorCode.REQUIRED_FIELD);
        }

        MailRequest mailRequest = MailRequest.builder()
                .to(user.getEmail())
                .subject("Thông báo xóa tài khoản")
                .text("Xin chào " + user.getFullName() + ",\n\n"
                        + "Tài khoản của bạn đã bị chặn bởi quản trị viên với lý do: "
                        + request.getReason() + ".\n"
                        + "Nếu bạn cho rằng đây là nhầm lẫn, vui lòng liên hệ bộ phận hỗ trợ.\n\n"
                        + "Trân trọng.")
                .build();

        sendSimpleMessage(mailRequest);

        user.setDeletedAt(LocalDateTime.now());

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Delete)
                .description("Xóa mềm người dùng: " + user.getFullName())
                .module(ActionActicityModule.User)
                .objectID(user.getId())
                .build());

        userRepositories.save(user);
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void DeleteOne(int id) {
        var user = userRepositories.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepositories.deleteById(id);

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Delete)
                .description("Xóa người dùng: " + user.getFullName())
                .module(ActionActicityModule.User)
                .objectID(user.getId())
                .build());
    }

    @PreAuthorize("hasAnyAuthority('MANAGE_USERS','MANAGE_ORDERS')")
    public List<UserResponse> List(boolean active) {
        return userRepositories.findAll(active).stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public PageResponse<UserResponse> Get(int page, int size, boolean active) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = userRepositories.findAll(pageable, active);

        var data =
                pageData.getContent().stream().map(userMapper::toUserResponse).collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS') or hasRole('ADMIN') or hasRole('MANAGER')")
    public PageResponse<UserResponse> GetAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = userRepositories.findAll(pageable);

        var data =
                pageData.getContent().stream().map(userMapper::toUserResponse).collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    // Api client
    public UserResponse Register(RegisterRequest request) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(PredefinedRole.ROLE_CUSTOMER).build());

        if (userRepositories.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (userRepositories.existsByPhone(request.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        User user = userMapper.toUserRegister(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepositories.save(user);

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Create)
                .description("Đăng ký người dùng: " + user.getFullName())
                .module(ActionActicityModule.User)
                .objectID(user.getId())
                .build());

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepositories
                .findByIdAndNotDeleted(Integer.valueOf(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(userRepositories.save(user));
    }

    // Api client
    public UserResponse UpdateProfile(int id, UpdateProfileRequest request) {
        try {
            User user = userRepositories
                    .findByIdAndNotDeleted(id)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            userMapper.updateProfile(user, request);

            activitylogService.create(ActivityRequest.builder()
                    .action(ActionActicityLog.Update)
                    .description("Cập nhập thông tin người dùng: " + user.getFullName())
                    .module(ActionActicityModule.User)
                    .objectID(user.getId())
                    .build());

            return userMapper.toUserResponse(userRepositories.save(user));
        } catch (AppException e) {
            throw new AppException(ErrorCode.ERROR_UPDATE_USER);
        }
    }

    // Api client and ...
    public Boolean changePassword(ChangePasswordRequest request) {
        if (request.getEmail().isEmpty()) {
            var u = getMyInfo();
            var user = userRepositories
                    .findById(u.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
            }

            if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
                throw new AppException(ErrorCode.NEW_PASSWORD_NOT_DUPLICATE_CONFIRM_PASSWORD);
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            userRepositories.save(user);

            activitylogService.create(ActivityRequest.builder()
                    .action(ActionActicityLog.Update)
                    .description("Cập nhập mật khẩu người dùng: " + user.getFullName())
                    .module(ActionActicityModule.User)
                    .objectID(user.getId())
                    .build());

            return true;
        }

        var user = userRepositories
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INCORRECT));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_NOT_DUPLICATE_CONFIRM_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepositories.save(user);

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Update)
                .description("Cập nhập mật khẩu người dùng")
                .module(ActionActicityModule.User)
                .build());

        return true;
    }

    public PageResponse<UserResponse> search(String keyword, String roleName, boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> pageData;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasRoleName = roleName != null && !roleName.trim().isEmpty();

        if (hasKeyword && hasRoleName) {
            pageData = userRepositories.findByKeywordAndRoleName(keyword.trim(), roleName.trim(), pageable);
        } else if (hasKeyword) {
            pageData = userRepositories.findByEmailOrFullNameOrPhoneContaining(keyword.trim(), pageable);
        } else if (hasRoleName) {
            pageData = userRepositories.findByRoleName(roleName.trim(), pageable);
        } else {
            pageData = userRepositories.findAll(pageable, active);
        }

        var data =
                pageData.getContent().stream().map(userMapper::toUserResponse).collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public PageResponse<UserResponse> getDeletedUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = userRepositories.findDeletedUsers(pageable);

        var data =
                pageData.getContent().stream().map(userMapper::toUserResponse).collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public UserResponse restoreUser(int id) {
        User user = userRepositories.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getDeletedAt() == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        user.setDeletedAt(null);
        user.setUpdatedAt(LocalDateTime.now());
        userRepositories.save(user);

        activitylogService.create(ActivityRequest.builder()
                .action(ActionActicityLog.Update)
                .description("Khôi phục người dùng: " + user.getFullName())
                .module(ActionActicityModule.User)
                .objectID(user.getId())
                .build());

        return userMapper.toUserResponse(user);
    }
}
