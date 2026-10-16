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
public class CreateProductRequestDTO {
    private String title;
    private String description;
    private String image;
    private BigDecimal price;
    private String category;
    private String rating;
}
