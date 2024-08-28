package com.baedalping.delivery.domain.store.repository;

import com.baedalping.delivery.domain.store.entity.Store;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, UUID> {

}
