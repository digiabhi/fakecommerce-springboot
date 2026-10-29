package com.example.fakecommerce.repositories;

import com.example.fakecommerce.schema.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
