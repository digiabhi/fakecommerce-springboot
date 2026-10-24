package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateReviewRequestDTO;
import com.example.fakecommerce.repositories.OrderRepository;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.repositories.ReviewRepository;
import com.example.fakecommerce.schema.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    public List<Review> getReviewsByOrderId(Long orderId) {
        return reviewRepository.findAllByOrderId(orderId);
    }

    public Review createReview(CreateReviewRequestDTO requestDTO) {
        Review review = Review.builder()
                .comment(requestDTO.getComment())
                .rating(requestDTO.getRating())
                .product(productRepository.findById(requestDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found")))
                .order(orderRepository.findById(requestDTO.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Order not found")))
                .build();
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        reviewRepository.delete(getReviewById(id));
    }
}
