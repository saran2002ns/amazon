package com.backend.amazon.service;

import com.backend.amazon.dto.OrderRequest;
import com.backend.amazon.dto.OrderResponse;
import com.backend.amazon.dto.ProductResponse;
import com.backend.amazon.dto.UserResponse;
import com.backend.amazon.model.Order;
import com.backend.amazon.model.OrderItem;
import com.backend.amazon.model.Product;
import com.backend.amazon.model.User;
import com.backend.amazon.repository.OrderRepository;
import com.backend.amazon.repository.ProductRepository;
import com.backend.amazon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponse> getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToResponse);
    }

    public OrderResponse createOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setPaymentMethod(request.getPaymentMethod());

        // Calculate total amount and create order items
        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(
                                    () -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPriceAtTime(product.getPriceCents());
                    orderItem.setDeliveryOption(itemRequest.getDeliveryOption());

                    return orderItem;
                })
                .collect(Collectors.toList());

        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPriceAtTime().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Clear the user's cart after successful order
        cartService.clearCart(userId);

        return convertToResponse(savedOrder);
    }

    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    public List<OrderResponse> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getUserOrdersByStatus(Long userId, String status) {
        return orderRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Check if order can be cancelled (only PENDING orders can be cancelled)
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("Only pending orders can be cancelled");
        }

        order.setStatus("CANCELLED");
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setOrderDate(order.getOrderDate());
        response.setDeliveryDate(order.getDeliveryDate());

        // Convert user to UserResponse
        User user = order.getUser();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        response.setUser(userResponse);

        // Convert order items to OrderItemResponse
        List<OrderResponse.OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        response.setItems(orderItemResponses);

        return response;
    }

    private OrderResponse.OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        OrderResponse.OrderItemResponse response = new OrderResponse.OrderItemResponse();
        response.setId(orderItem.getId());
        response.setQuantity(orderItem.getQuantity());
        response.setPriceAtTime(orderItem.getPriceAtTime());
        response.setDeliveryOption(orderItem.getDeliveryOption());

        // Convert product to ProductResponse
        Product product = orderItem.getProduct();
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