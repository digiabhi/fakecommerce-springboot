package com.example.fakecommerce.services;

import com.example.fakecommerce.adapters.OrderAdapter;
import com.example.fakecommerce.dtos.CreateOrderRequestDTO;
import com.example.fakecommerce.dtos.GetOrderResponseDTO;
import com.example.fakecommerce.dtos.OrderSummaryResponseDTO;
import com.example.fakecommerce.dtos.UpdateOrderRequestDTO;
import com.example.fakecommerce.exceptions.InvalidRequestException;
import com.example.fakecommerce.exceptions.ResourceDeletionException;
import com.example.fakecommerce.exceptions.ResourceNotFoundException;
import com.example.fakecommerce.repositories.OrderProductRepository;
import com.example.fakecommerce.repositories.OrderRepository;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.schema.OrderProduct;
import com.example.fakecommerce.schema.OrderStatus;
import com.example.fakecommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderAdapter orderAdapter;

    public List<GetOrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderAdapter.mapToGetOrderResponseDTOList(orders);
    }

    public GetOrderResponseDTO getOrderById(Long id) {
        return orderAdapter.mapToGetOrderResponseDTO(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found")));
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with id " + id + " not found"));
        orderRepository.delete(order);
    }

    public void createOrder(CreateOrderRequestDTO createOrderRequestDTO){
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .build();

        orderRepository.save(order);

        if(createOrderRequestDTO.getOrderItems()!= null) {
//            for(var itemDTO : createOrderRequestDTO.getOrderItems()) {
//                Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(()-> new ResourceNotFoundException("Product with id " + itemDTO.getProductId() + " not found"));
//
//                OrderProduct orderProduct = OrderProduct.builder()
//                        .order(order)
//                        .product(product)
//                        .quantity(itemDTO.getQuantity() != null ? itemDTO.getQuantity() : 1)
//                        .build();
//
//                orderProductRepository.save(orderProduct);
//            }
            List<Long> productIds = createOrderRequestDTO.getOrderItems().stream()
                    .map(item -> item.getProductId())
                    .collect(Collectors.toList());
            List<Product> products = productRepository.findAllById(productIds);

            products.forEach(product -> {
                var itemDTO = createOrderRequestDTO.getOrderItems().stream()
                        .filter(item -> item.getProductId().equals(product.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Product with id " + product.getId() + " not found"));

                OrderProduct orderProduct = OrderProduct.builder()
                        .order(order)
                        .product(product)
                        .quantity(itemDTO.getQuantity() != null ? itemDTO.getQuantity() : 1)
                        .build();

                orderProductRepository.save(orderProduct);
            });


        }
    }
}
