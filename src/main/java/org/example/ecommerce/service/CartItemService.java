package org.example.ecommerce.service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.ecommerce.model.Cart;
import org.example.ecommerce.model.CartItem;
import org.example.ecommerce.model.Product;
import org.example.ecommerce.model.User;
import org.example.ecommerce.repository.CartItemRepository;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;


    public List<CartItem> getAllCartItems(Cart cart) {
        if (cart == null) {
            return Collections.emptyList(); // Return an empty list if no cart exists
        }
        return cartItemRepository.findByCart(cart);
    }


    @Transactional
    public boolean updateCartItem(Integer cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Product product = productRepository.findById(cartItem.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (quantity > 0) {
            if (product.getStock() >= quantity) {
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
            } else {
                throw new RuntimeException("Quantity exceeds maximum stocks available");
            }

            return true;
        } else {
            throw new RuntimeException("Quantity must be greater than 0");
        }
    }

    @Transactional
    public boolean removeCartItem(Integer cartItemId) {
        if (cartItemRepository.existsById(cartItemId)) {
            cartItemRepository.deleteById(cartItemId);
            return true;
        }
        return false;
    }

    public boolean clearCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return true;
        }

        cart.getCartItems().clear();
        cartRepository.save(cart);
        return true;
    }



}
