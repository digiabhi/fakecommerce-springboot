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
public class OrderItemResponseDTO {
    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal productPrice;

    private Integer quantity;

    private BigDecimal subTotal;
    
}
