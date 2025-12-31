package com.example.geniegoods.controller;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
public class GoodsRestController {

    private final GoodsService goodsService;

    @GetMapping("/test")
    // 로그인한 유저 정보 얻고싶을때 @AuthenticationPrincipal UserEntity user 이렇게 할것
    public ResponseEntity<UserEntity> test(@AuthenticationPrincipal UserEntity user) {

        return ResponseEntity.ok(user);
    }

}
