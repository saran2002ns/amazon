package com.backend.amazon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String id;
    private String image;
    private String name;
    private Double ratingStars;
    private Integer ratingCount;
    private BigDecimal priceCents;
    private List<String> keywords;
    private String type;
    private String sizeChartLink;
}