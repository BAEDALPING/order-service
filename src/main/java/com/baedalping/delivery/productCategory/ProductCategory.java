package com.baedalping.delivery.productCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_product_categories")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_category_id")
    private UUID productCategoryId;

    @Column(nullable = false)
    private String productCategoryName;

    private boolean isPublic = true;
}
