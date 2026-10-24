package com.example.fakecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderSummaryResponseDTO {
    private Long orderId;
    private Long userId;
    private String status;
    private Integer totalItems;
    private BigDecimal totalAmount;
}
