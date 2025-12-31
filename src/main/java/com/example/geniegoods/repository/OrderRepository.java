package com.example.geniegoods.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.geniegoods.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {}