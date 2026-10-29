package com.example.fakecommerce.services;

import com.example.fakecommerce.adapters.OrderAdapter;
import com.example.fakecommerce.dtos.*;
import com.example.fakecommerce.exceptions.ResourceNotFoundException;
import com.example.fakecommerce.repositories.OrderProductRepository;
import com.example.fakecommerce.repositories.OrderRepository;
import com.example.fakecommerce.repositories.ProductRepository;
import com.example.fakecommerce.schema.Order;
import com.example.fakecommerce.schema.OrderProduct;
import com.example.fakecommerce.schema.OrderStatus;
import com.example.fakecommerce.schema.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Transactional
    public GetOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO){
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

            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));

            List<OrderProduct> orderProducts = new ArrayList<>();

            for(var itemDTO : createOrderRequestDTO.getOrderItems()){
                Product product = productMap.get(itemDTO.getProductId());

                OrderProduct orderProduct = OrderProduct.builder()
                        .order(order)
                        .product(product)
                        .quantity(itemDTO.getQuantity() != null ? itemDTO.getQuantity() : 1)
                        .build();

                orderProducts.add(orderProduct);
            }

            orderProductRepository.saveAll(orderProducts);
        }
        return orderAdapter.mapToGetOrderResponseDTO(order);
    }

    @Transactional
    public GetOrderResponseDTO updateOrder(Long id, UpdateOrderRequestDTO updateOrderRequestDTO){
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order not found with id: " + id));

        if(updateOrderRequestDTO.getStatus()!=null){
            order.setStatus(updateOrderRequestDTO.getStatus());
            orderRepository.save(order);
        }

        if(updateOrderRequestDTO.getOrderItems()!=null){
            List<Long> productIds = updateOrderRequestDTO.getOrderItems().stream()
                    .map(item -> item.getProductId())
                    .collect(Collectors.toList());
            System.out.println(productIds);

            List<Product> products = productRepository.findAllById(productIds);

            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));

            for(var pid : productIds){
                if(!productMap.containsKey(pid)){
                    throw new ResourceNotFoundException("Product not found with id: " + pid);
                }
            }

            List<OrderProduct> toSave = new ArrayList<>();
            List<OrderProduct> toDelete = new ArrayList<>();

            Map<Long, OrderProduct> existingItems = orderProductRepository.findByOrderWithProduct(order).stream().collect(Collectors.toMap(op -> op.getProduct().getId(), Function.identity()));

            for(OrderItemActionDTO itemActionDTO : updateOrderRequestDTO.getOrderItems()){
                Product product = productMap.get(itemActionDTO.getProductId());

                OrderProduct existing = existingItems.get(product.getId());

                switch(itemActionDTO.getAction()){
                    case ADD -> {
                        if (existing != null) {
                            int addQty = (itemActionDTO.getQuantity() != null) ? itemActionDTO.getQuantity() : 1;
                            existing.setQuantity(existing.getQuantity() + addQty);
                            toSave.add(existing);
                        } else {
                            OrderProduct newItem = OrderProduct.builder().order(order).product(product).quantity(itemActionDTO.getQuantity() != null ? itemActionDTO.getQuantity() : 1).build();
                            existingItems.put(product.getId(), newItem);
                            toSave.add(newItem);
                        }
                    }
                    case REMOVE -> {
                        if(existing == null){
                         throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        toDelete.add(existing);
                        existingItems.remove(product.getId());
                    }
                    case INCREMENT -> {
                        if(existing == null){
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        existing.setQuantity(existing.getQuantity() + 1);
                        toSave.add(existing);
                    }
                    case DECREMENT -> {
                        if(existing == null){
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        if(existing.getQuantity() <= 1){
                            toDelete.add(existing);
                            existingItems.remove(product.getId());
                        } else {
                            existing.setQuantity(existing.getQuantity() - 1);
                            toSave.add(existing);
                        }
                    }
                }
            }

            if(!toSave.isEmpty()){
                orderProductRepository.saveAll(toSave);
            }
            if(!toDelete.isEmpty()){
                orderProductRepository.deleteAll(toDelete);
            }
        }

        return orderAdapter.mapToGetOrderResponseDTO(order);
    }

    public GetOrderSummaryResponseDTO getOrderSummary(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        List<OrderProduct> orderProducts = orderProductRepository.findByOrderWithProduct(order);

        List<OrderItemResponseDTO> items = orderAdapter.mapToOrderItemResponseDTO(orderProducts);

        int totalItems = orderProducts.stream().mapToInt(OrderProduct::getQuantity).sum();

        BigDecimal totalPrice = orderProducts.stream().map(op-> op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        return GetOrderSummaryResponseDTO.builder().id(order.getId()).status(order.getStatus()).orderItems(items).totalItems(totalItems).totalPrice(totalPrice).createdAt(order.getCreatedAt()).updatedAt(order.getUpdatedAt()).build();
    }
}
