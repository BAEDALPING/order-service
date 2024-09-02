package com.baedalping.delivery.domain.user.repository;

import com.baedalping.delivery.domain.user.entity.UserAddress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
  Optional<UserAddress> findByAddressId(UUID addressId);
}
