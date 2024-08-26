package com.baedalping.delivery.store;

import com.baedalping.delivery.product.Product;
import com.baedalping.delivery.productCategory.ProductCategory;
import com.baedalping.delivery.storeCategory.StoreCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_stores")
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_id")
  private UUID storeId;

  @Column(nullable = false)
  private String storeName;

  /*@OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;*/

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_category_id",nullable = false)
  private StoreCategory storeCategory;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> productList = new ArrayList<>();

  private String storePhone;

  private String storeAddress;

  @Column(columnDefinition = "TEXT")
  private String storeDetail;

  private Time openTime;

  private Time closeTime;

  private boolean isPublic = true;

}
