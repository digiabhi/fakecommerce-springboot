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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewAdapter reviewAdapter;

    public List<GetReviewResponseDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviewAdapter.mapToGetReviewResponseDTOList(reviews);
    }

    public GetReviewResponseDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        return reviewAdapter.mapToGetReviewResponseDTO(review);
    }

    public List<GetReviewResponseDTO> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviewAdapter.mapToGetReviewResponseDTOList(reviews);
    }

    public List<GetReviewResponseDTO> getReviewsByOrderId(Long orderId) {
        List<Review> reviews = reviewRepository.findAllByOrderId(orderId);
        return reviewAdapter.mapToGetReviewResponseDTOList(reviews);
    }

    public GetReviewResponseDTO createReview(CreateReviewRequestDTO requestDTO) {
        if (requestDTO == null
                || requestDTO.getProductId() == null
                || requestDTO.getOrderId() == null
                || requestDTO.getRating() == null) {
            throw new InvalidRequestException("Review productId, orderId, and rating are required");
        }
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + requestDTO.getProductId() + " not found"));
        Order order = orderRepository.findById(requestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with id " + requestDTO.getOrderId() + " not found"));

        Review review = Review.builder()
                .comment(requestDTO.getComment())
                .rating(requestDTO.getRating())
                .product(product)
                .order(order)
                .build();
        Review savedReview = reviewRepository.save(review);
        return reviewAdapter.mapToGetReviewResponseDTO(savedReview);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        try {
            reviewRepository.delete(review);
        } catch (DataAccessException ex) {
            throw new ResourceDeletionException("Review with id " + id + " could not be deleted", ex);
        }
    }
}
