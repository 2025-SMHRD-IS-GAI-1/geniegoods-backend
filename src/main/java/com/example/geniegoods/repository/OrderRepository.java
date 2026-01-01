package com.example.geniegoods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.geniegoods.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    
    /**
     * 특정 접두사로 시작하는 주문번호 개수 조회
     * @param prefix 주문번호 접두사 (예: "20240512")
     * @return 해당 접두사로 시작하는 주문번호 개수
     */
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderNumber LIKE :prefix%")
    long countByOrderNumberStartingWith(@Param("prefix") String prefix);
}