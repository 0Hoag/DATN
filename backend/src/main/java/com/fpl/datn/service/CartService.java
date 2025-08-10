package com.fpl.datn.service;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.fpl.datn.dto.request.AddCartRequest;
import com.fpl.datn.dto.request.ChangeCartItemRequest;
import com.fpl.datn.dto.response.CartItemResponse;
import com.fpl.datn.dto.response.CartResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.CartItemMapper;
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
    CartItemMapper cartItemMapper;
    AuthenticationService authenticationService;

    public CartResponse addToCart(AddCartRequest request, HttpSession session) {
        Integer variantId = request.getVariantId();

        Cart cart = getOrCreateCart(session);

        var variant = productVariantRepository
                .findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));

        CartItem cartItems = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), variantId)
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .productVariant(variant)
                        .quantity(0)
                        .price(variant.getPrice())
                        .build());

        cartItems.setQuantity(cartItems.getQuantity() + request.getQuantity());
        cartItemRepository.save(cartItems);
        return mapper.toCartResponse(cart);
    }

    public List<CartItemResponse> getCartItems(HttpSession session) {
        String sessionId = session.getId();
        Integer userId = authenticationService.extractUserIdFromSecurityContext();
        Cart cart = (userId == null)
                ? repository.findBySessionId(sessionId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED))
                : repository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        var cartItems = cartItemRepository.findByCartId(cart.getId());
        return cartItems.stream()
                .map(cartItem -> cartItemMapper.toCartItemResponse(cartItem))
                .toList();
    }

    @Transactional
    public CartItemResponse changeCartItemQuantity(HttpSession session, ChangeCartItemRequest request) {
        String sessionId = session.getId();
        Integer userId = authenticationService.extractUserIdFromSecurityContext();
        Integer variantId = request.getVariantId();
        Integer quantity = request.getQuantity();
        Cart cart = (userId == null)
                ? repository.findBySessionId(sessionId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED))
                : repository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), variantId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem); // Xoá nếu = 0
        } else {
            cartItem.setQuantity(quantity); // Gán trực tiếp số lượng mới
            cartItemRepository.save(cartItem);
        }
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    @Transactional
    public void delete(HttpSession session, Integer variantId) {
        String sessionId = session.getId();
        Integer userId = authenticationService.extractUserIdFromSecurityContext();
        Cart cart = (userId == null)
                ? repository.findBySessionId(sessionId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED))
                : repository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), variantId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void mergeSessionCartToUser(HttpSession session) {
        String sessionId = session.getId();
        Integer userId = authenticationService.extractUserIdFromSecurityContext();
        if (userId == null) return;

        var sessionCartOpt = repository.findBySessionId(sessionId);
        if (sessionCartOpt.isEmpty()) return;

        Cart sessionCart = sessionCartOpt.get();
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Cart userCart = repository.findByUserId(userId).orElseGet(() -> {
            var cart = Cart.builder().createdAt(LocalDate.now()).user(user).build();

            return repository.save(cart);
        });
        List<CartItem> sessionItems = cartItemRepository.findByCartId(sessionCart.getId());
        for (CartItem sessionItem : sessionItems) {
            mergeItem(sessionItem, userCart);
        }
        cartItemRepository.deleteAll(sessionItems);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        repository.save(cart);
    }

    public Cart getOrCreateCart(HttpSession session) {
        String sessionId = session.getId();
        Integer userId = authenticationService.extractUserIdFromSecurityContext();
        return userId != null
                ? repository.findByUserId(userId).orElseGet(() -> createCartForUser(userId))
                : repository.findBySessionId(sessionId).orElseGet(() -> createCartForSession(sessionId));
    }

    public Cart getCartByUser(Integer userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
    }

    public Cart getCartBySession(String sessionId) {
        return repository.findBySessionId(sessionId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXISTED));
    }

    private Cart createCartForUser(Integer userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var cart = Cart.builder().user(user).createdAt(LocalDate.now()).build();
        return repository.save(cart);
    }

    private Cart createCartForSession(String sessionId) {
        var cart =
                Cart.builder().sessionId(sessionId).createdAt(LocalDate.now()).build();
        return repository.save(cart);
    }

    private void mergeItem(CartItem sessionItem, Cart userCart) {
        var variant = sessionItem.getProductVariant();
        var existingItemOpt = cartItemRepository.findByCartIdAndProductVariantId(
                userCart.getId(), sessionItem.getProductVariant().getId());
        int stockQuantity = variant.getQuantity();
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int requestedQuantity = existingItem.getQuantity() + sessionItem.getQuantity();
            existingItem.setQuantity(Math.min(requestedQuantity, stockQuantity));
            existingItem.setPrice(variant.getPrice());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(userCart);
            newItem.setProductVariant(sessionItem.getProductVariant());
            newItem.setQuantity(sessionItem.getQuantity());
            newItem.setPrice(variant.getPrice());
            cartItemRepository.save(newItem);
        }
    }
}
