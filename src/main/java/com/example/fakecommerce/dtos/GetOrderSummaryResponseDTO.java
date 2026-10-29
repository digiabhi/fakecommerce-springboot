package com.example.fakecommerce.dtos;

import com.example.fakecommerce.schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderSummaryResponseDTO {

    private Long id;

    private OrderStatus status;

    private List<OrderItemResponseDTO> orderItems;

    private Integer totalItems;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
