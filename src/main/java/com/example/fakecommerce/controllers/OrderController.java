package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateOrderRequestDTO;
import com.example.fakecommerce.dtos.GetOrderResponseDTO;
import com.example.fakecommerce.dtos.GetOrderSummaryResponseDTO;
import com.example.fakecommerce.dtos.UpdateOrderRequestDTO;
import com.example.fakecommerce.services.OrderService;
import com.example.fakecommerce.utils.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<GetOrderResponseDTO>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders(), "Orders retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(orderService.createOrder(createOrderRequestDTO), "Order created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Order deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> getOrderById(@PathVariable Long id) {
        GetOrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(order, "Order retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequestDTO updateOrderRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success(orderService.updateOrder(id, updateOrderRequestDTO), "Order updated successfully"));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiResponse<GetOrderSummaryResponseDTO>> getOrderSummary(@PathVariable Long id) {
        GetOrderSummaryResponseDTO orderSummary = orderService.getOrderSummary(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(orderSummary, "Order summary retrieved successfully"));
    }
}
