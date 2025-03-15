package org.example.ecommerce.repository;

import org.eclipse.angus.mail.imap.protocol.Item;
import org.example.ecommerce.model.cart.Cart;
import org.example.ecommerce.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Item> {
    Cart findByUser(User user);
}
