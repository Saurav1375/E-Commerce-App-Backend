package org.example.ecommerce.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.enums.ProductCategory;
import org.example.ecommerce.model.product.Product;
import org.example.ecommerce.repository.CartItemRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public boolean updateProduct(Integer id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    boolean updated = false;

                    if (product.getName() != null && !product.getName().trim().isEmpty()) {
                        existingProduct.setName(product.getName());
                        updated = true;
                    }
                    if (product.getDescription() != null && !product.getDescription().trim().isEmpty()) {
                        existingProduct.setDescription(product.getDescription());
                        updated = true;
                    }
                    if (product.getProductCategory() != null) {
                        existingProduct.setProductCategory(product.getProductCategory());
                        updated = true;
                    }
                    if (product.getImage_url() != null && !product.getImage_url().trim().isEmpty()) {
                        existingProduct.setImage_url(product.getImage_url());
                        updated = true;
                    }
                    if (product.getPriceUsd() != null) {
                        existingProduct.setPriceUsd(product.getPriceUsd());
                        updated = true;
                    }
                    if (product.getStock() != null) {
                        existingProduct.setStock(product.getStock());
                        updated = true;
                    }

                    if (updated) {
                        productRepository.save(existingProduct);
                    }
                    return updated;
                })
                .orElse(false);
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            ProductCategory productCategory = ProductCategory.valueOf(category.toUpperCase());
            return productRepository.findAllByProductCategory(productCategory);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid category: " + category);
        }
    }

    public List<Product> searchProductByName(String name) {
        try {
            return productRepository.findAllByNameContainingIgnoreCase(name);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid name: " + name);
        }
    }

    @Transactional
    public boolean deleteProductById(Integer id) {
        try {
            Product product = productRepository.findById(id).orElse(null);
            cartItemRepository.deleteByProduct(product); // Delete all cart items linked to the product
            productRepository.deleteById(id); // Now safely delete the product
            return true;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid id: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product with id: " + id, e);
        }
    }

}
