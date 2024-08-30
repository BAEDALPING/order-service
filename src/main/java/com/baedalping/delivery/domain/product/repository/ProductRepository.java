package com.baedalping.delivery.domain.product.repository;

import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.store.entity.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  List<Product> findAllByStore(Store store);

}
