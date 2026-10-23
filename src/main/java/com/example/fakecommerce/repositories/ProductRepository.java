package com.example.fakecommerce.repositories;

import com.example.fakecommerce.schema.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory(String category);

//    @Query(nativeQuery = true, value = "SELECT DISTINCT category FROM products")
    @Query("SELECT DISTINCT category FROM Product")
    List<String> findAllUniqueCategories();

//    @Query(nativeQuery = true, value = "SELECT p.*,c.name FROM products p INNER JOIN categories c ON p.category_id = c.id WHERE p.id = :id")
    @Query("SELECT p FROM Product p JOIN FETCH p.category c WHERE p.id = :id")
    List<Product> findProductsWithDetailsById(Long id);
}
