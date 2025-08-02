package com.backend.amazon.controller;

import com.backend.amazon.dto.CartRequest;
import com.backend.amazon.dto.CartResponse;
import com.backend.amazon.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponse>> getUserCart(@PathVariable Long userId) {
        List<CartResponse> cartItems = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long userId, @RequestBody CartRequest request) {
        CartResponse cartItem = cartService.addToCart(userId, request);
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(@PathVariable Long cartItemId,
            @RequestBody CartRequest request) {
        CartResponse cartItem = cartService.updateCartItem(cartItemId, request);
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long userId, @PathVariable String productId) {
        boolean removed = cartService.removeFromCart(userId, productId);
        return removed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/count")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
        Integer count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{userId}/total")
    public ResponseEntity<BigDecimal> getCartTotal(@PathVariable Long userId) {
        BigDecimal total = cartService.getCartTotal(userId);
        return ResponseEntity.ok(total);
    }
}