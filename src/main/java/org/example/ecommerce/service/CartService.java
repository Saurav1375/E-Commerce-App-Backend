package org.example.ecommerce.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.model.cart.Cart;
import org.example.ecommerce.model.cart.CartItem;
import org.example.ecommerce.model.product.Product;
import org.example.ecommerce.model.user.User;
import org.example.ecommerce.repository.CartItemRepository;
import org.example.ecommerce.repository.CartRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    public Cart getUserCart(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        return user.map(cartRepository::findByUser).orElse(null);
    }


    @Transactional
    public boolean addCartItem(String username, Integer productId, int quantity) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart); // Save cart if not exists
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item is already in cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            if(product.getStock() >= quantity + cartItem.getQuantity()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
            } else {
                throw new RuntimeException("Not enough stock");
            }
        } else {
            CartItem newCartItem = new CartItem(cart, product, quantity);
            if(product.getStock() >= quantity) {
                cartItemRepository.save(newCartItem);
            } else {
                throw new RuntimeException("Not enough stock");
            }
        }
        return true;
    }




}
