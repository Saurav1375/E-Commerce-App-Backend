package org.example.ecommerce.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.enums.OrderStatus;
import org.example.ecommerce.model.order.Order;
import org.example.ecommerce.model.order.OrderItem;
import org.example.ecommerce.model.order.OrderRequest;
import org.example.ecommerce.model.product.Product;
import org.example.ecommerce.model.user.User;
import org.example.ecommerce.repository.OrderItemRepository;
import org.example.ecommerce.repository.OrderRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getUserOrdersByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Order> orders = orderRepository.findAllByUser(user);

        if (orders.isEmpty()) {
            throw new RuntimeException("No orders found for user");
        }

        return orders;
    }

    @Transactional
    public Order placeOrder(String email, OrderRequest orderRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (itemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than zero");
            }

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPriceUsd() * itemRequest.getQuantity());

            return orderItem;
        }).toList();

        Double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();


        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING);

        orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }

        return order;
    }

    public Order getOrderByIdAndUser(Integer orderId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found or unauthorized access"));
    }

    @Transactional
    public void cancelOrder(Integer orderId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found or unauthorized access"));

        // Only allow cancellation if the order is not already shipped or delivered
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order cannot be canceled as it has already been shipped or delivered.");
        }

        // Restore stock for each product in the order
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        // Set order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public Order updateOrderStatus(Integer orderId, String newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }

        Order order = optionalOrder.get();

        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Invalid order status: " + newStatus);
        }

        order.setStatus(OrderStatus.valueOf(newStatus.toUpperCase()));
        return orderRepository.save(order);
    }

    private boolean isValidStatus(String status) {
        try {
            OrderStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
