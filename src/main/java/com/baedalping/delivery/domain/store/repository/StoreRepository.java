package com.baedalping.delivery.domain.store.repository;

import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.storeCategory.entity.StoreCategory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, UUID> {
  List<Store> findAllByStoreCategory(StoreCategory storeCategory);

  Page<Store> findAllByOrderByCreatedAtAscUpdatedAtAsc(Pageable pageable);

  List<Store> findAllByStoreNameContaining(String keyword);
}
