package org.example.ecommerce.model.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    private Integer productId;
    private int quantity;

}
