package com.baedalping.delivery.domain.product.productCategory.repository;

import com.baedalping.delivery.domain.product.productCategory.entity.ProductCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
  Optional<ProductCategory> findByProductCategoryName(String producCategoryName);

  Page<ProductCategory> findAllByOrderByCreatedAtAscUpdatedAtAsc(Pageable pageable);
}
