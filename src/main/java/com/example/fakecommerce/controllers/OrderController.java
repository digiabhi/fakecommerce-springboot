package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateOrderRequestDTO;
import com.example.fakecommerce.dtos.GetOrderResponseDTO;
import com.example.fakecommerce.dtos.OrderSummaryResponseDTO;
import com.example.fakecommerce.dtos.UpdateOrderRequestDTO;
import com.example.fakecommerce.schema.Order;
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
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> createOrder() {
        throw new UnsupportedOperationException("Order creation is not supported in this version of the API");
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
}
