package com.fpl.datn.configuration;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                var system_backup = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.SYSTEM_BACKUP)
                        .description("Sao lưu / phục hồi dữ liệu hệ thống")
                        .build());

                var system_settings = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.SYSTEM_SETTINGS)
                        .description("Thay đổi cấu hình hệ thống (email, tích hợp, bảo mật...)")
                        .build());

                var assign_role = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.ASSIGN_ROLE)
                        .description("Cấp / thu hồi quyền cho người dùng khác")
                        .build());

                var manager_products = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.MANAGE_PRODUCTS)
                        .description("Quản lý toàn bộ sản phẩm trong hệ thống")
                        .build());

                var manager_users = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.MANAGE_USERS)
                        .description("Xem, chỉnh sửa, khóa tài khoản người dùng")
                        .build());

                var manager_orders = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.MANAGE_ORDERS)
                        .description("Xem và cập nhật trạng thái đơn hàng toàn hệ thống")
                        .build());

                var track_order = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.TRACK_ORDER)
                        .description("Theo dõi đơn hàng đã đặt")
                        .build());

                var view_order = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIEW_ORDER)
                        .description("Xem đơn hàng")
                        .build());

                var create_review = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.CREATE_REVIEW)
                        .description("Viết đánh giá sản phẩm")
                        .build());

                var buy_product = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.BUY_PRODUCT)
                        .description("Thêm vào giỏ hàng, thanh toán")
                        .build());

                var view_product = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIEW_PRODUCT)
                        .description("Xem danh sách & chi tiết sản phẩm")
                        .build());

                var view_dashboard = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIEW_DASHBOARD)
                        .description("Xem thống kê && doanh thu")
                        .build());

                Set<Permission> guestPermissions = Set.of(view_product);
                Set<Permission> CustomerPermissions =
                        Set.of(view_product, track_order, create_review, buy_product, view_order);
                Set<Permission> ShiftStaff =
                        Set.of(view_product, track_order, create_review, buy_product, view_order, manager_orders);
                Set<Permission> ManagerPermissions = Set.of(
                        view_product,
                        track_order,
                        create_review,
                        buy_product,
                        view_order,
                        manager_users,
                        manager_products,
                        manager_orders);
                Set<Permission> AdminPermissions = Set.of(assign_role, system_settings, system_backup, view_dashboard);

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_GUEST)
                        .description("Guest role")
                        .permissions(guestPermissions)
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_CUSTOMER)
                        .description("Customer role")
                        .permissions(CustomerPermissions)
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_SHIFT_STAFF)
                        .description("Shift staff role")
                        .permissions(ShiftStaff)
                        .build());

                Role managerRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_MANAGER)
                        .description("Manager role")
                        .permissions(ManagerPermissions)
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ROLE_ADMIN)
                        .description("Admin role")
                        .permissions(AdminPermissions)
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);
                roles.add(managerRole);

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
