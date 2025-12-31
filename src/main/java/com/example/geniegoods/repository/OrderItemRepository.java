package com.example.geniegoods.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.geniegoods.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
