package org.example.ecommerce.service;


import lombok.RequiredArgsConstructor;
import org.example.ecommerce.model.order.Order;
import org.example.ecommerce.model.order.OrderItem;
import org.example.ecommerce.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    public Map<Order, List<OrderItem>> getAllOrderItems(List<Order> orders) {
        Map<Order, List<OrderItem>> orderItemsMap = new HashMap<>();

        for (Order order : orders) {
            List<OrderItem> orderItems = orderItemRepository.findAllByOrder(order);
            orderItemsMap.put(order, orderItems);
        }

        return orderItemsMap;
    }
}
