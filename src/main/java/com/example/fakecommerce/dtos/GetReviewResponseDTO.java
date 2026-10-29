package com.example.fakecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetReviewResponseDTO {
    private Long id;

    private Long productId;

    private Long orderId;

    private BigDecimal rating;

    private String comment;

    private LocalDateTime createdAt;
}
