package org.example.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.ecommerce.enums.ProductCategory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Integer productId;
    private String name;
    private String description;
    private Double priceUsd;
    private String image_url;
    private Integer stock;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory productCategory;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}
