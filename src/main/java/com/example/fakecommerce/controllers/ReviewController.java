package com.example.fakecommerce.controllers;

import com.example.fakecommerce.dtos.CreateReviewRequestDTO;
import com.example.fakecommerce.schema.Review;
import com.example.fakecommerce.services.ReviewService;
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
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody CreateReviewRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Review>> getReviewsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(reviewService.getReviewsByOrderId(orderId));
    }
}
