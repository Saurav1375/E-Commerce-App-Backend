package org.example.ecommerce.repository;

import org.example.ecommerce.model.cart.Cart;
import org.example.ecommerce.model.cart.CartItem;
import org.example.ecommerce.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    void deleteByProduct(Product product);
}
