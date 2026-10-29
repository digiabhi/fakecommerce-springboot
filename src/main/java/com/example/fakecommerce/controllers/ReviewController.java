package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateReviewRequestDTO;
import com.example.fakecommerce.dtos.GetReviewResponseDTO;
import com.example.fakecommerce.services.ReviewService;
import com.example.fakecommerce.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetReviewResponseDTO>>> getAllReviews() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews(), "Reviews retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GetReviewResponseDTO>> createReview(@RequestBody CreateReviewRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(reviewService.createReview(requestDTO), "Review created successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetReviewResponseDTO>> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewById(id), "Review retrieved successfully"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<GetReviewResponseDTO>>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProductId(productId),
                "Reviews retrieved successfully"));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<GetReviewResponseDTO>>> getReviewsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByOrderId(orderId),
                "Reviews retrieved successfully"));
    }
}
