package com.baedalping.delivery.product;


import com.baedalping.delivery.productCategory.ProductCategory;
import com.baedalping.delivery.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(precision = 7, scale = 0, nullable = false)
    private BigDecimal productPrice;

    @Column(columnDefinition = "TEXT")
    private String productDetail;

    @Column(columnDefinition = "TEXT")
    private String productImgUrl;

    private boolean isPublic = true;

}
