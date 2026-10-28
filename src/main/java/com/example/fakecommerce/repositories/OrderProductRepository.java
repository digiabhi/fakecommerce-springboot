package com.example.fakecommerce.repositories;

import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.schema.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(Long orderId);

    @Query("SELECT op FROM OrderProduct op JOIN FETCH op.product WHERE op.order = :order")
    List<OrderProduct> findByOrderWithProduct(Order order);
}
