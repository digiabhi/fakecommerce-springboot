package com.example.fakecommerce.repositories;

import com.example.fakecommerce.schema.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProductId(Long productId);

    List<Review> findAllByOrderId(Long orderId);
}
