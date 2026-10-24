package com.example.fakecommerce.repositories;

import com.example.fakecommerce.schema.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findAllByOrderId(Long orderId);
}
