package com.baedalping.delivery.storeCategory;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_store_categories")
public class StoreCategory extends AuditField {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "store_category_id")
  private UUID storeCategoryId;

  @Column(nullable = false)
  private String storeCategoryName;

  private boolean isPublic = true;

}
