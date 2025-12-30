package com.example.geniegoods.controller;

import com.example.geniegoods.entity.UserEntity;
import com.example.geniegoods.repository.UserRepository;
import com.example.geniegoods.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /**
     * 토큰 발급 (포스트맨 테스트용)
     */
    @GetMapping("/token/{userId}")
    public ResponseEntity<Map<String, String>> getToken(@PathVariable("userId") Long userId) {

        // 1. 존재 여부 체크
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        // JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getNickname());

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);

        return ResponseEntity.ok(response);
    }
}
