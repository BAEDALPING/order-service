package com.baedalping.delivery.domain.review.repository;

import com.baedalping.delivery.domain.review.entity.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByStore_StoreIdAndIsReportedFalse(UUID storeId);

    List<Review> findByStore_StoreId(UUID storeId);

    // 가게의 평균 평점 계산
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.storeId = :storeId AND r.isReported = false")
    Double findAverageRatingByStoreId(@Param("storeId") UUID storeId);
}

