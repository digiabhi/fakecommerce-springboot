package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateOrderRequestDTO;
import com.example.fakecommerce.dtos.OrderSummaryResponseDTO;
import com.example.fakecommerce.dtos.UpdateOrderRequestDTO;
import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id,
                                             @RequestBody UpdateOrderRequestDTO requestDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, requestDTO));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<OrderSummaryResponseDTO> getOrderSummary(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderSummary(id));
    }
}
