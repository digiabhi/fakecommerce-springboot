package com.example.fakecommerce.services;

import com.example.fakecommerce.adapters.ReviewAdapter;
import com.example.fakecommerce.dtos.CreateReviewRequestDTO;
import com.example.fakecommerce.dtos.GetReviewResponseDTO;
import com.example.fakecommerce.exceptions.InvalidRequestException;
import com.example.fakecommerce.exceptions.ResourceDeletionException;
import com.example.fakecommerce.exceptions.ResourceNotFoundException;
import com.example.fakecommerce.repositories.OrderRepository;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.repositories.ReviewRepository;
import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.schema.Product;
import com.example.fakecommerce.schema.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewAdapter reviewAdapter;

    public List<GetReviewResponseDTO> getAllReviews() {
        log.info("Fetching all reviews");
        List<Review> reviews = reviewRepository.findAll();
        return reviewAdapter.mapToGetReviewResponseDTOList(reviews);
    }

    public GetReviewResponseDTO getReviewById(Long id) {
        log.info("Fetching review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Review with id {} not found", id);
                    return new ResourceNotFoundException("Review with id " + id + " not found");
                });
        return reviewAdapter.mapToGetReviewResponseDTO(review);
    }

    public List<GetReviewResponseDTO> getReviewsByProductId(Long productId) {
        log.info("Fetching reviews for productId: {}", productId);
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviewAdapter.mapToGetReviewResponseDTOList(reviews);
    }

    public List<GetReviewResponseDTO> getReviewsByOrderId(Long orderId) {
        log.info("Fetching reviews for orderId: {}", orderId);
        List<Review> reviews = reviewRepository.findAllByOrderId(orderId);
        return reviewAdapter.mapToGetReviewResponseDTOList(reviews);
    }

    public GetReviewResponseDTO createReview(CreateReviewRequestDTO requestDTO) {
        if (requestDTO == null
                || requestDTO.getProductId() == null
                || requestDTO.getOrderId() == null
                || requestDTO.getRating() == null) {
            log.error("Failed to create review: productId, orderId, and rating are required");
            throw new InvalidRequestException("Review productId, orderId, and rating are required");
        }
        log.info("Creating review for productId: {}, orderId: {}", requestDTO.getProductId(), requestDTO.getOrderId());
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> {
                    log.error("Product with id {} not found when creating review", requestDTO.getProductId());
                    return new ResourceNotFoundException(
                            "Product with id " + requestDTO.getProductId() + " not found");
                });
        Order order = orderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> {
                    log.error("Order with id {} not found when creating review", requestDTO.getOrderId());
                    return new ResourceNotFoundException(
                            "Order with id " + requestDTO.getOrderId() + " not found");
                });

        Review review = Review.builder()
                .comment(requestDTO.getComment())
                .rating(requestDTO.getRating())
                .product(product)
                .order(order)
                .build();
        Review savedReview = reviewRepository.save(review);
        log.info("Review created successfully with id: {}", savedReview.getId());
        return reviewAdapter.mapToGetReviewResponseDTO(savedReview);
    }

    public void deleteReview(Long id) {
        log.info("Deleting review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Review with id {} not found for deletion", id);
                    return new ResourceNotFoundException("Review with id " + id + " not found");
                });
        try {
            reviewRepository.delete(review);
            log.info("Review with id {} deleted successfully", id);
        } catch (DataAccessException ex) {
            log.error("Error deleting review with id {}", id, ex);
            throw new ResourceDeletionException("Review with id " + id + " could not be deleted", ex);
        }
    }
}
