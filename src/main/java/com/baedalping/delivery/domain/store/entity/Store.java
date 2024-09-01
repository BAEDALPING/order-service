package com.baedalping.delivery.domain.store.entity;

import com.baedalping.delivery.domain.store.dto.StoreRequestDto;
import com.baedalping.delivery.global.common.AuditField;
import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
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

  private boolean isPublic = false;


  public void addStoreCategory(StoreCategory storeCategory){
    this.storeCategory = storeCategory;
    storeCategory.getStoreList().add(this);
  }

  public Store(StoreRequestDto storeRequestDto, StoreCategory storeCategory){
    this.storeName = storeRequestDto.getStoreName();
    this.storePhone = storeRequestDto.getStorePhone();
    this.storeAddress = storeRequestDto.getStoreAddress();
    this.storeDetail = storeRequestDto.getStoreDetail();
    this.openTime = storeRequestDto.getOpenTime();
    this.closeTime = storeRequestDto.getCloseTime();
    this.storeCategory = storeCategory;
  }

  public void updateStore(StoreRequestDto StoreRequestDto, StoreCategory storeCategory){
    this.storeName = StoreRequestDto.getStoreName();
    this.storePhone = StoreRequestDto.getStorePhone();
    this.storeAddress = StoreRequestDto.getStoreAddress();
    this.storeDetail = StoreRequestDto.getStoreDetail();
    this.openTime = StoreRequestDto.getOpenTime();
    this.closeTime = StoreRequestDto.getCloseTime();
    this.storeCategory = storeCategory;
  }
}
