package com.baedalping.delivery.domain.store.storeCategory.repository;

import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
  Optional<StoreCategory> findByStoreCategoryName(String storeCategoryName);

}
