package com.example.fakecommerce.dtos;

import com.example.fakecommerce.schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequestDTO {
    private Long userId;
    private OrderStatus status;
}
