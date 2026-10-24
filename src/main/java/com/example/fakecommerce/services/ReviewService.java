package com.example.fakecommerce.services;

import com.example.fakecommerce.dtos.CreateReviewRequestDTO;
import com.example.fakecommerce.exceptions.InvalidRequestException;
import com.example.fakecommerce.exceptions.ResourceDeletionException;
import com.example.fakecommerce.exceptions.ResourceNotFoundException;
import com.example.fakecommerce.repositories.OrderRepository;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.repositories.ReviewRepository;
import com.example.fakecommerce.schema.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
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
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    public List<Review> getReviewsByOrderId(Long orderId) {
        return reviewRepository.findAllByOrderId(orderId);
    }

    public Review createReview(CreateReviewRequestDTO requestDTO) {
        if (requestDTO == null
                || requestDTO.getProductId() == null
                || requestDTO.getOrderId() == null
                || requestDTO.getRating() == null) {
            throw new InvalidRequestException("Review productId, orderId, and rating are required");
        }
        Review review = Review.builder()
                .comment(requestDTO.getComment())
                .rating(requestDTO.getRating())
                .product(productRepository.findById(requestDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Product with id " + requestDTO.getProductId() + " not found")))
                .order(orderRepository.findById(requestDTO.getOrderId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Order with id " + requestDTO.getOrderId() + " not found")))
                .build();
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        try {
            reviewRepository.delete(review);
        } catch (DataAccessException ex) {
            throw new ResourceDeletionException("Review with id " + id + " could not be deleted", ex);
        }
    }
}
