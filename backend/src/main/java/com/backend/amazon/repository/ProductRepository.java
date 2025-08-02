package com.backend.amazon.repository;

import com.backend.amazon.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM products p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR EXISTS (SELECT k FROM p.keywords k WHERE LOWER(k) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchProducts(@Param("keyword") String keyword);

    List<Product> findByType(String type);

    @Query("SELECT p FROM products p WHERE p.rating.stars >= :minRating")
    List<Product> findByRatingGreaterThanEqual(@Param("minRating") Double minRating);

    @Query("SELECT p FROM products p WHERE p.priceCents BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice,
            @Param("maxPrice") java.math.BigDecimal maxPrice);

    Optional<Product> findById(String id);
}