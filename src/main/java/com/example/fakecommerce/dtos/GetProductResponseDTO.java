package com.example.fakecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GetProductResponseDTO {
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private String image;

    private String rating;
}
