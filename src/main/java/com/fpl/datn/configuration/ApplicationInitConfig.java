package com.fpl.datn.configuration;

import com.fpl.datn.constant.PredefinedPermission;
import com.fpl.datn.constant.PredefinedRole;
import com.fpl.datn.models.Permission;
import com.fpl.datn.models.Role;
import com.fpl.datn.models.User;
import com.fpl.datn.repository.PermissionRepository;
import com.fpl.datn.repository.RoleRepository;
import com.fpl.datn.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.DELETE_PRODUCT_ANY)
                        .description("Xóa bất kỳ sản phẩm nào")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.DELETE_USER_ANY)
                        .description("Xóa bất kỳ tài khoản nào")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.SYSTEM_BACKUP)
                        .description("Sao lưu / phục hồi dữ liệu hệ thống")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.SYSTEM_SETTINGS)
                        .description("Thay đổi cấu hình hệ thống (email, tích hợp, bảo mật...)")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.ASSIGN_ROLE)
                        .description("Cấp / thu hồi quyền cho người dùng khác")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.MANAGE_PRODUCTS)
                        .description("Quản lý toàn bộ sản phẩm trong hệ thống")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.MANAGE_USERS)
                        .description("Xem, chỉnh sửa, khóa tài khoản người dùng")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.MANAGE_ORDERS)
                        .description("Xem và cập nhật trạng thái đơn hàng toàn hệ thống")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.TRACK_ORDER)
                        .description("Theo dõi đơn hàng đã đặt")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.CREATE_REVIEW)
                        .description("Viết đánh giá sản phẩm")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.BUY_PRODUCT)
                        .description("Thêm vào giỏ hàng, thanh toán")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIEW_PRODUCT)
                        .description("Xem danh sách & chi tiết sản phẩm")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_GUEST)
                        .description("Guest role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_CUSTOMER)
                        .description("Customer role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_MANAGER)
                        .description("Manager role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_SHIFT_STAFF)
                        .description("Shift staff role")
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_ADMIN)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
