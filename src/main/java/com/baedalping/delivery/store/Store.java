package com.baedalping.delivery.store;

import com.baedalping.delivery.global.common.AuditField;
import com.baedalping.delivery.product.Product;
import com.baedalping.delivery.storeCategory.StoreCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_stores")
public class Store extends AuditField {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_id")
  private UUID storeId;

  @Column(nullable = false)
  private String storeName;

  /*@OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;*/

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_category_id", nullable = false)
  private StoreCategory storeCategory;

  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> productList = new ArrayList<>();

  private String storePhone;

  private String storeAddress;

  @Column(columnDefinition = "TEXT")
  private String storeDetail;

  private LocalTime openTime;

  private LocalTime closeTime;

  private boolean isPublic = true;


  public void addStoreCategory(StoreCategory storeCategory){
    this.storeCategory = storeCategory;
    storeCategory.getStoreList().add(this);
  }

  public Store(StoreCreateRequestDto storeCreateRequestDto, StoreCategory storeCategory){
    this.storeName = storeCreateRequestDto.getStoreName();
    this.storePhone = storeCreateRequestDto.getStorePhone();
    this.storeAddress = storeCreateRequestDto.getStoreAddress();
    this.storeDetail = storeCreateRequestDto.getStoreDetail();
    this.openTime = storeCreateRequestDto.getOpenTime();
    this.closeTime = storeCreateRequestDto.getCloseTime();
    this.storeCategory = storeCategory;
  }

}
