package com.example.fakecommerce.adapters;

import com.example.fakecommerce.dtos.GetOrderResponseDTO;
import com.example.fakecommerce.dtos.OrderItemResponseDTO;
import com.example.fakecommerce.repositories.OrderProductRepository;
import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.schema.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final OrderProductRepository orderProductRepository;

    public List<GetOrderResponseDTO> mapToGetOrderResponseDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::mapToGetOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public GetOrderResponseDTO mapToGetOrderResponseDTO(Order order){

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(order.getId());
        List<OrderItemResponseDTO> orderItems = mapToOrderItemResponseDTO(orderProducts);

        return GetOrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(orderItems)
                .build();
    }

    public List<OrderItemResponseDTO> mapToOrderItemResponseDTO(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(op -> OrderItemResponseDTO.builder()
                        .productId(op.getProduct().getId())
                        .productName(op.getProduct().getTitle())
                        .productPrice(op.getProduct().getPrice())
                        .productImage(op.getProduct().getImage())
                        .quantity(op.getQuantity())
                        .subTotal(op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                        .build())
                .collect(Collectors.toList());
    }
}
