package com.baedalping.delivery.domain.store.storeCategory.entity;

import com.baedalping.delivery.domain.store.storeCategory.dto.StoreCategoryCreateRequestDto;
import com.baedalping.delivery.global.common.AuditField;
import com.baedalping.delivery.domain.store.entity.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_store_categories")
public class StoreCategory extends AuditField {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_category_id")
  private UUID storeCategoryId;

  @Column(nullable = false, unique = true)
  private String storeCategoryName;

  @OneToMany(mappedBy = "storeCategory")
  private List<Store> storeList = new ArrayList<>();

  private boolean isPublic = true;

  public StoreCategory(StoreCategoryCreateRequestDto storeCategoryCreateRequestDto){
    this.storeCategoryName = storeCategoryCreateRequestDto.getStoreCategoryName();
  }
}
