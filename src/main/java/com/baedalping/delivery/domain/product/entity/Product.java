package com.baedalping.delivery.domain.product.entity;


import com.baedalping.delivery.domain.product.dto.ProductCreateRequestDto;
import com.baedalping.delivery.domain.product.dto.ProductUpdateRequestDto;
import com.baedalping.delivery.global.common.AuditField;
import com.baedalping.delivery.domain.productCategory.entity.ProductCategory;
import com.baedalping.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_products")
public class Product extends AuditField {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Integer productPrice;

    @Column(columnDefinition = "TEXT")
    private String productDetail;

    @Column(columnDefinition = "TEXT")
    private String productImgUrl;

    private boolean isPublic = true;


    public void addStore(Store store){
        this.store = store;
        store.getProductList().add(this);
    }

    public void addProductCategory(ProductCategory productCategory){
        this.productCategory = productCategory;
        productCategory.getProductList().add(this);
    }

    public Product(ProductCreateRequestDto productCreateRequestDto, ProductCategory productCategory, Store store){
        this.productName = productCreateRequestDto.getProductName();
        this.productCategory = productCategory;
        this.store = store;
        this.productPrice = productCreateRequestDto.getProductPrice();
        this.productDetail = productCreateRequestDto.getProductDetail();
        this.productImgUrl = productCreateRequestDto.getProductImgUrl();
    }

    public void updateProduct(ProductUpdateRequestDto productUpdateRequestDto, ProductCategory productCategory, Store store){
        this.productName = productUpdateRequestDto.getProductName();
        this.productCategory = productCategory;
        this.store = store;
        this.productPrice = productUpdateRequestDto.getProductPrice();
        this.productDetail = productUpdateRequestDto.getProductDetail();
        this.productImgUrl = productUpdateRequestDto.getProductImgUrl();
    }

}
