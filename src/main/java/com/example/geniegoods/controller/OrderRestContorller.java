package com.example.geniegoods.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.geniegoods.dto.OrderRequestDto;
import com.example.geniegoods.entity.OrderEntity;
import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestContorller {
	
	private final OrderService orderService;

    @PostMapping("/create")
    // 로그인한 유저 정보 얻고싶을때 @AuthenticationPrincipal UserEntity user 이렇게 할것
    public ResponseEntity<OrderEntity> createOrder(@AuthenticationPrincipal UserEntity user, @RequestBody OrderRequestDto dto) {
        return ResponseEntity.ok(orderService.createOrder(user.getUserId(), dto));
    }
	

}
