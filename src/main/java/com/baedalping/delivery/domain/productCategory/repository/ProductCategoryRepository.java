package com.baedalping.delivery.domain.productCategory.repository;

import com.baedalping.delivery.domain.productCategory.entity.ProductCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
  Optional<ProductCategory> findByProductCategoryName(String producCategoryName);
}
