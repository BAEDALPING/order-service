package com.baedalping.delivery.productCategory;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_product_categories")
public class ProductCategory extends AuditField {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_category_id")
    private UUID productCategoryId;

    @Column(nullable = false)
    private String productCategoryName;

    private boolean isPublic = true;
}
