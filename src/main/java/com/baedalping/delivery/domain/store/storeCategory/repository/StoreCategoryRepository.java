package com.baedalping.delivery.domain.store.storeCategory.repository;

import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
  Optional<StoreCategory> findByStoreCategoryName(String storeCategoryName);

  //이런건 키워드로 검색시 사용함
  //@Query("SELECT sc FROM StoreCategory sc ORDER BY sc.createdAt, sc.updatedAt")
  //Page<StoreCategory> findAll(Pageable pageable);

  Page<StoreCategory> findAllByOrderByCreatedAtAscUpdatedAtAsc(Pageable pageable);

}
