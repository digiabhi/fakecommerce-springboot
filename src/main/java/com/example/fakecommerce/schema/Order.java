package com.example.fakecommerce.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="orders")
@SQLDelete(sql = "UPDATE orders SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Order extends BaseEntity {

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OrderStatus status;

//    @ManyToMany
//    @JoinTable(
//            name = "order_products",
//            joinColumns = @JoinColumn(name = "order_id"), // the FK belonging to same schema - Order
//            inverseJoinColumns = @JoinColumn(name = "product_id") // the FK belonging to the other schema - Product
//    )
//    private List<Product> products;
}
