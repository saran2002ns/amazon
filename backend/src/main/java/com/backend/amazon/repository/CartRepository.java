package com.backend.amazon.repository;

import com.backend.amazon.model.Cart;
import com.backend.amazon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    @Query("SELECT c FROM carts c WHERE c.user.id = :userId")
    List<Cart> findByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM carts c WHERE c.user.id = :userId AND c.product.id = :productId")
    Optional<Cart> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") String productId);

    @Query("SELECT COUNT(c) FROM carts c WHERE c.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    void deleteByUserAndProduct(User user, com.backend.amazon.model.Product product);
}