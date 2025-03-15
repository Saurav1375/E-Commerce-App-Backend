package org.example.ecommerce.repository;

import org.example.ecommerce.enums.ProductCategory;
import org.example.ecommerce.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByProductCategory(ProductCategory productCategory);

    List<Product> findAllByNameContainingIgnoreCase(String name);
}
