package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateReviewRequestDTO;
import com.example.fakecommerce.dtos.GetReviewResponseDTO;
import com.example.fakecommerce.services.ReviewService;
import com.example.fakecommerce.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetReviewResponseDTO>>> getAllReviews() {
        log.info("REST request to get all reviews");
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews(), "Reviews retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GetReviewResponseDTO>> createReview(@RequestBody CreateReviewRequestDTO requestDTO) {
        log.info("REST request to create review for productId: {}, orderId: {}",
                requestDTO != null ? requestDTO.getProductId() : null,
                requestDTO != null ? requestDTO.getOrderId() : null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(reviewService.createReview(requestDTO), "Review created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        log.info("REST request to delete review with id: {}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetReviewResponseDTO>> getReviewById(@PathVariable Long id) {
        log.info("REST request to get review with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewById(id), "Review retrieved successfully"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<GetReviewResponseDTO>>> getReviewsByProductId(@PathVariable Long productId) {
        log.info("REST request to get reviews for productId: {}", productId);
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProductId(productId),
                "Reviews retrieved successfully"));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<GetReviewResponseDTO>>> getReviewsByOrderId(@PathVariable Long orderId) {
        log.info("REST request to get reviews for orderId: {}", orderId);
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByOrderId(orderId),
                "Reviews retrieved successfully"));
    }
}
