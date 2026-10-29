package com.example.fakecommerce.adapters;

import com.example.fakecommerce.dtos.GetReviewResponseDTO;
import com.example.fakecommerce.schema.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewAdapter {

    public List<GetReviewResponseDTO> mapToGetReviewResponseDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(this::mapToGetReviewResponseDTO)
                .collect(Collectors.toList());
    }

    public GetReviewResponseDTO mapToGetReviewResponseDTO(Review review) {
        if (review == null) {
            return null;
        }

        return GetReviewResponseDTO.builder()
                .id(review.getId())
                .productId(review.getProduct() != null ? review.getProduct().getId() : null)
                .orderId(review.getOrder() != null ? review.getOrder().getId() : null)
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
