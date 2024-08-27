package com.baedalping.delivery.storeCategory;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
  Optional<StoreCategory> findByStoreCategoryName(String storeCategoryName);
}
