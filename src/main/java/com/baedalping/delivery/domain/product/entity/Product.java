package com.baedalping.delivery.domain.product.entity;


import com.baedalping.delivery.domain.product.dto.ProductRequestDto;
import com.baedalping.delivery.domain.product.productCategory.entity.ProductCategory;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Product(ProductRequestDto productRequestDto, ProductCategory productCategory, Store store){
        this.productName = productRequestDto.getProductName();
        this.productCategory = productCategory;
        this.store = store;
        this.productPrice = productRequestDto.getProductPrice();
        this.productDetail = productRequestDto.getProductDetail();
        this.productImgUrl = productRequestDto.getProductImgUrl();
    }

    public void updateProduct(ProductRequestDto productRequestDto, ProductCategory productCategory, Store store){
        this.productName = productRequestDto.getProductName();
        this.productCategory = productCategory;
        this.store = store;
        this.productPrice = productRequestDto.getProductPrice();
        this.productDetail = productRequestDto.getProductDetail();
        this.productImgUrl = productRequestDto.getProductImgUrl();
    }

}
