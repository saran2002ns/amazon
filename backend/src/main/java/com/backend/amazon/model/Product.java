package com.backend.amazon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    @Id
    private String id;

    private String image;
    private String name;

    @Embedded
    private Rating rating;

    private BigDecimal priceCents;

    @ElementCollection
    @CollectionTable(name = "product_keywords", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "keyword")
    private List<String> keywords;

    private String type;
    private String sizeChartLink;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rating {
        private Double stars;
        private Integer count;
    }
}
