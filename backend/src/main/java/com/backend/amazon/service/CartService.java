package com.backend.amazon.service;

import com.backend.amazon.dto.CartRequest;
import com.backend.amazon.dto.CartResponse;
import com.backend.amazon.dto.ProductResponse;
import com.backend.amazon.model.Cart;
import com.backend.amazon.model.Product;
import com.backend.amazon.model.User;
import com.backend.amazon.repository.CartRepository;
import com.backend.amazon.repository.ProductRepository;
import com.backend.amazon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CartResponse> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CartResponse addToCart(Long userId, CartRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> existingCartItem = cartRepository.findByUserIdAndProductId(userId, request.getProductId());

        if (existingCartItem.isPresent()) {
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            if (request.getDeliveryOption() != null) {
                cartItem.setDeliveryOption(request.getDeliveryOption());
            }
            Cart savedCart = cartRepository.save(cartItem);
            return convertToResponse(savedCart);
        } else {
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(request.getQuantity());
            newCartItem.setDeliveryOption(request.getDeliveryOption() != null ? request.getDeliveryOption() : "1");

            Cart savedCart = cartRepository.save(newCartItem);
            return convertToResponse(savedCart);
        }
    }

    public CartResponse updateCartItem(Long cartItemId, CartRequest request) {
        Cart cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (request.getQuantity() != null) {
            cartItem.setQuantity(request.getQuantity());
        }
        if (request.getDeliveryOption() != null) {
            cartItem.setDeliveryOption(request.getDeliveryOption());
        }

        Cart savedCart = cartRepository.save(cartItem);
        return convertToResponse(savedCart);
    }

    public boolean removeFromCart(Long userId, String productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<Cart> cartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem.isPresent()) {
            cartRepository.delete(cartItem.get());
            return true;
        }
        return false;
    }

    public void clearCart(Long userId) {
        List<Cart> userCart = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(userCart);
    }

    public Integer getCartItemCount(Long userId) {
        return cartRepository.countByUserId(userId).intValue();
    }

    public BigDecimal getCartTotal(Long userId) {
        List<Cart> userCart = cartRepository.findByUserId(userId);
        return userCart.stream()
                .map(cart -> cart.getProduct().getPriceCents().multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartResponse convertToResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setQuantity(cart.getQuantity());
        response.setDeliveryOption(cart.getDeliveryOption());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());

        // Convert product to ProductResponse
        Product product = cart.getProduct();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setImage(product.getImage());
        productResponse.setName(product.getName());
        productResponse.setPriceCents(product.getPriceCents());
        productResponse.setKeywords(product.getKeywords());
        productResponse.setType(product.getType());
        productResponse.setSizeChartLink(product.getSizeChartLink());

        ProductResponse.RatingResponse ratingResponse = new ProductResponse.RatingResponse();
        ratingResponse.setStars(product.getRating().getStars());
        ratingResponse.setCount(product.getRating().getCount());
        productResponse.setRating(ratingResponse);

        response.setProduct(productResponse);

        return response;
    }
}