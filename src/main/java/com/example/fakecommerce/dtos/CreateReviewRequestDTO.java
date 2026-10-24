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
public class CreateReviewRequestDTO {
    private String comment;
    private Long productId;
    private Long orderId;
    private BigDecimal rating;
}
