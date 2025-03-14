package org.example.ecommerce.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {
    ELECTRONICS(1, "Electronics"),
    FASHION(2, "Fashion"),
    HOME(3, "Home Appliances"),
    BOOKS(4, "Books"),
    BEAUTY(5, "Beauty & Personal Care"),
    SPORTS(6, "Sports & Outdoors"),
    TOYS(7, "Toys & Games"),
    GROCERIES(8, "Groceries"),
    AUTOMOTIVE(9, "Automotive"),
    FURNITURE(10, "Furniture");

    private final int id;
    @Getter
    private final String name;

    public static ProductCategory getByName(String name) {
        for (ProductCategory category : values()) {
            if (category.name.equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category name: " + name);
    }



}

