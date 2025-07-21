package com.fpl.datn.service;

import java.time.LocalDate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.response.CartResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.CartMapper;
import com.fpl.datn.models.Cart;
import com.fpl.datn.models.CartItem;
import com.fpl.datn.repository.CartItemRepository;
import com.fpl.datn.repository.CartRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {
    CartRepository repository;
    CartItemRepository cartItemRepository;
    ProductVariantRepository productVariantRepository;
    UserRepository userRepository;
    CartMapper mapper;

    private Cart createCartForUser(Integer userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDate.now());
        return repository.save(cart);
    }

    private Cart createCartForSession(String sessionId) {
        Cart cart = new Cart();
        cart.setSessionId(sessionId);
        cart.setCreatedAt(LocalDate.now());
        return repository.save(cart);
    }

    private Integer getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return Integer.valueOf(auth.getName());
    }

    public CartResponse getOrCreateCart(String sessionId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Kiểm tra nếu đã đăng nhập (không phải anonymousUser)
        if (authentication != null
                && authentication.isAuthenticated()
                && !("anonymousUser".equals(authentication.getName()))) {
            String userIdStr = authentication.getName();
            Integer userId = Integer.valueOf(userIdStr);

            return mapper.toCartResponse(repository.findByUserId(userId).orElseGet(() -> createCartForUser(userId)));
        }

        // Nếu chưa đăng nhập, dùng sessionId
        return mapper.toCartResponse(
                repository.findBySessionId(sessionId).orElseGet(() -> createCartForSession(sessionId)));
    }

    public void addToCart(Integer cartId, Integer variantId, Integer quantity) {
        var cart = repository.findById(cartId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        var variant = productVariantRepository
                .findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        CartItem cartItems = cartItemRepository
                .findByCartIdAndProductVariantId(cartId, variantId)
                .orElseGet(() -> {
                    CartItem newItem = CartItem.builder()
                            .cart(cart)
                            .productVariant(variant)
                            .quantity(0)
                            .price(variant.getPrice())
                            .build();
                    return newItem;
                });
        cartItems.setQuantity(cartItems.getQuantity() + quantity);
        cartItemRepository.save(cartItems);
    }
}
