package com.baedalping.delivery.domain.product.productCategory.entity;

import com.baedalping.delivery.domain.product.productCategory.dto.ProductCategoryRequestDto;
import com.baedalping.delivery.global.common.AuditField;
import com.baedalping.delivery.domain.product.entity.Product;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_product_categories")
public class ProductCategory extends AuditField {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_category_id")
    private UUID productCategoryId;

    @Column(nullable = false, unique = true)
    private String productCategoryName;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> productList = new ArrayList<>();

    private boolean isPublic = true;

    public ProductCategory(ProductCategoryRequestDto productCategoryRequestDto){
        this.productCategoryName = productCategoryRequestDto.getProductCategoryName();
    }
}
