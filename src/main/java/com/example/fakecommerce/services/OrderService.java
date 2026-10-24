package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateOrderRequestDTO;
import com.example.fakecommerce.dtos.OrderSummaryResponseDTO;
import com.example.fakecommerce.dtos.UpdateOrderRequestDTO;
import com.example.fakecommerce.repositories.OrderProductRepository;
import com.example.fakecommerce.repositories.OrderRepository;
import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.schema.OrderProduct;
import com.example.fakecommerce.schema.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order createOrder(CreateOrderRequestDTO requestDTO) {
        Order order = Order.builder()
                .userId(requestDTO.getUserId())
                .status(requestDTO.getStatus() == null ? OrderStatus.PENDING : requestDTO.getStatus())
                .build();
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, UpdateOrderRequestDTO requestDTO) {
        Order order = getOrderById(id);
        if (requestDTO.getUserId() != null) {
            order.setUserId(requestDTO.getUserId());
        }
        if (requestDTO.getStatus() != null) {
            order.setStatus(requestDTO.getStatus());
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.delete(getOrderById(id));
    }

    @Transactional(readOnly = true)
    public OrderSummaryResponseDTO getOrderSummary(Long id) {
        Order order = getOrderById(id);
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrderId(id);
        int totalItems = orderProducts.stream()
                .mapToInt(orderProduct -> orderProduct.getQuantity() == null ? 0 : orderProduct.getQuantity())
                .sum();
        BigDecimal totalAmount = orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(orderProduct.getQuantity() == null ? 0 : orderProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderSummaryResponseDTO.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus().name())
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .build();
    }
}
