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

}
