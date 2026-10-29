package com.example.fakecommerce.dtos;

import com.example.fakecommerce.schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderRequestDTO {
    private OrderStatus status;

    private List<OrderItemActionDTO> orderItems;

}
