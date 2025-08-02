package com.backend.amazon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
    private String deliveryOption;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CartSummaryResponse {
    private List<CartResponse> items;
    private Integer totalItems;
    private BigDecimal totalAmount;
}