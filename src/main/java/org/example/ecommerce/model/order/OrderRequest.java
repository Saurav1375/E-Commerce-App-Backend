package org.example.ecommerce.model.order;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class OrderRequest {
    private List<OrderItemRequest> orderItems = new ArrayList<>(); // Initialize list

}
