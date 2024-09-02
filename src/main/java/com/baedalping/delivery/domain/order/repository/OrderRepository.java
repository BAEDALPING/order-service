package com.baedalping.delivery.domain.order.repository;

import com.baedalping.delivery.domain.order.entity.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> ,
    JpaSpecificationExecutor<Order> {

    Page<Order> findByStore_StoreIdAndIsPublicTrue(UUID storeId, Pageable pageable);

    Page<Order> findByUser_UserIdAndIsPublicTrue(Long userId, Pageable pageable);


    // JPQL을 사용하여 특정 키워드를 포함하는 모든 결과를 검색
    @Query("SELECT DISTINCT o FROM Order o " +
        "JOIN o.store s " +
        "JOIN s.storeCategory sc " +
        "JOIN o.orderDetails od " +
        "JOIN od.product p " +
        "JOIN p.productCategory pc " +
        "WHERE o.isPublic = true " +
        "AND (" +
        "LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(sc.storeCategoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(pc.productCategoryName) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
        ")")
    Page<Order> findOrdersByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
