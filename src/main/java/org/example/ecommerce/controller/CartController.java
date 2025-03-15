package org.example.ecommerce.controller;


import lombok.AllArgsConstructor;
import org.example.ecommerce.model.cart.Cart;
import org.example.ecommerce.model.cart.CartItem;
import org.example.ecommerce.model.cart.CartRequest;
import org.example.ecommerce.service.CartItemService;
import org.example.ecommerce.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getAllCartItems() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Cart userCart = cartService.getUserCart(username);
            List<CartItem> cartItems = cartItemService.getAllCartItems(userCart);
            if (cartItems.isEmpty()) {
                return new ResponseEntity<>(cartItems, HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> addCartItem(@RequestBody CartRequest cartRequest) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            boolean added = cartService.addCartItem(username, cartRequest.getProductId(), cartRequest.getQuantity());

            if (added) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Product added to cart successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add product to cart");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Integer cartItemId,
            @RequestParam int quantity
    ) {
        try {
            boolean updated = cartItemService.updateCartItem(cartItemId, quantity);
            if (updated) {
                return ResponseEntity.ok("Cart item updated successfully");
            }
            return ResponseEntity.badRequest().body("Invalid cart item ID or quantity");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Integer cartItemId) {
        try {
            boolean removed = cartItemService.removeCartItem(cartItemId);
            if (removed) {
                return ResponseEntity.ok("Cart item removed successfully");
            }
            return ResponseEntity.badRequest().body("Cart item not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            boolean cleared = cartItemService.clearCart(username);
            if (cleared) {
                return ResponseEntity.ok("Cart cleared successfully");
            }
            return ResponseEntity.badRequest().body("Cart is already empty");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



}
