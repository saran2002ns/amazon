package com.backend.amazon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String image;
    private String name;
    private RatingResponse rating;
    private BigDecimal priceCents;
    private List<String> keywords;
    private String type;
    private String sizeChartLink;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingResponse {
        private Double stars;
        private Integer count;
    }
}