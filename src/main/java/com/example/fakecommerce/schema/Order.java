package com.example.fakecommerce.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="orders")
public class Order extends BaseEntity {

    private OrderStatus status;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"), // the FK belonging to same schema - Order
            inverseJoinColumns = @JoinColumn(name = "product_id") // the FK belonging to the other schema - Product
    )
    private List<Product> products;
}
