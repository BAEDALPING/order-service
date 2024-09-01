package com.baedalping.delivery.domain.review.repository;

import com.baedalping.delivery.domain.review.entity.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByStore_StoreIdAndIsReportedFalse(UUID storeId);

    List<Review> findByStore_StoreId(UUID storeId);
}

