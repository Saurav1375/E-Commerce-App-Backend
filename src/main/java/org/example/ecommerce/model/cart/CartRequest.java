package org.example.ecommerce.model.cart;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartRequest {
    private Integer productId;
    private Integer quantity;
}
