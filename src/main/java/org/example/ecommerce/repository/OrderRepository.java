package org.example.ecommerce.repository;

import org.example.ecommerce.model.order.Order;
import org.example.ecommerce.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByUser(User user);

    Optional<Order> findByOrderIdAndUser(Integer orderId, User user);
}
